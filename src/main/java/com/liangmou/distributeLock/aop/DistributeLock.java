package com.liangmou.distributeLock.aop;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;


/**
 * 自定义 分布式锁注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributeLock {
    /**
     * 分布式锁的key
     */
    String key() ;

    /**
     * 分布式锁的超时时间，默认为5
     */
    long timeout() default 5;

    /**
     * 分布式锁的超时时间单位，默认为秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
