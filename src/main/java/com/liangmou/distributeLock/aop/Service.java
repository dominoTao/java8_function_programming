package com.liangmou.distributeLock.aop;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class Service {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AnnotationResolver annotationResolver;

//    @Around(value = "postTest()&&@annotation(distributeLock")
    @Around("execution(* com.liangmou.distributeLock.aop.BaseController.*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint, DistributeLock distributeLock) throws Exception {
        final String key = annotationResolver.resolver(joinPoint, distributeLock.key());
        final String lock = getLock(key, distributeLock.timeout(), distributeLock.timeUnit());
        if (StringUtil.isNullOrEmpty(lock)) {
            // 获取锁失败
            return BaseResponse.addError(ErrorCodeEnum.OPERATE_FAILED, ErrorCodeEnum.OPERATE_FAILED_MSG);
        }

        // 获取锁成功
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            return BaseResponse.addError(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR_MSG);
        } finally {
            // 释放锁。
            unLock(key, lock);
        }
    }

    /**
     * 获取分布式锁
     * @param key
     * @param timeout
     * @param timeUnit
     * @return
     */
    private String getLock(String key, long timeout, TimeUnit timeUnit) {
        try {
            final String value = UUID.randomUUID().toString();
            // setNX === SetOption.SET_IF_ABSENT
            Boolean lockStat = stringRedisTemplate.execute((RedisCallback<Boolean>) connect -> connect.set(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8),
                    Expiration.from(timeout, timeUnit), RedisStringCommands.SetOption.SET_IF_ABSENT));
            if (!lockStat) {
                //获取锁失败
                return null;
            }
            return value;
        } catch (Exception e) {
            log.error("获取分布式锁失败，key={}", key, e);
            return null;
        }
    }


    /**
     * 释放锁
     * @param key
     * @param value
     */
    private void unLock(String key, String value){
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            final Boolean unLockStat = stringRedisTemplate.execute((RedisCallback<? extends Boolean>) connect -> connect.eval(script.getBytes(StandardCharsets.UTF_8),
                    ReturnType.BOOLEAN, 1, key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8)));
            if (!unLockStat) {
                log.error("释放分布式锁失败，key={}，已自动超时，其他线程可能已经重新获取锁", key);
            }
        } catch (Exception e) {
            log.error("释放分布式锁失败，key={}", key, e);
        }
    }
}
