package com.liangmou.distributeLock.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.split;
/**
 * 分布式锁注解解析器
 */
@Component
public class AnnotationResolver {
    public String resolver(JoinPoint joinPoint, String toResolverStr) throws Exception {
        if (StringUtils.hasLength(toResolverStr)) {
            return null;
        }
        final StringBuffer sb = new StringBuffer();
        final String pattern = "#\\{(.+?)\\}";
        final Pattern compile = Pattern.compile(pattern);
        final Matcher matcher = compile.matcher(toResolverStr);
        while (matcher.find()) {
            final String key = matcher.group().replaceAll("#\\{", "").replaceAll("\\}", "");
            matcher.appendReplacement(sb, Objects.requireNonNull(key.contains(".") ? complexResolver(joinPoint, toResolverStr) : simpleResolver(joinPoint, toResolverStr)));
        }
        return sb.toString();
    }

    private String simpleResolver(JoinPoint joinPoint, String str) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            if (str.equals(parameterNames[i])) {
                return args[i].toString();
            }
        }
        return null;
    }


    private String complexResolver(JoinPoint joinPoint, String str) throws Exception {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        final String[] strs = split(str, "\\.");
        for (int i = 0; i < parameterNames.length; i++) {
            if (strs[0].equals(parameterNames[i])) {
                final Object arg = args[i];
                final Method declaredMethod = arg.getClass().getDeclaredMethod(getMethodName(strs[1]), null);
                final Object value = declaredMethod.invoke(args[i]);
                // todo 空指针问题
                return getValue(value, 1, strs).toString();
            }
        }
        return null;
    }

    private Object getValue(Object o, int index, String[] strs) {
        try {
            if (o != null && index < strs.length - 1) {
                final Method declaredMethod = o.getClass().getDeclaredMethod(getMethodName(strs[index + 1]), null);
                o = declaredMethod.invoke(o);
                getValue(o, index + 1, strs);
            }
            return o;
        } catch (Exception e) {
            return null;
        }
    }

    private String getMethodName(String name){
        return "get" + name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());

    }
}
