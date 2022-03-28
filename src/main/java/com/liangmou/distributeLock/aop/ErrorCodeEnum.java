package com.liangmou.distributeLock.aop;

public interface ErrorCodeEnum {

    int SYSTEM_ERROR = 1;
    String SYSTEM_ERROR_MSG = "系统异常";
    int OPERATE_FAILED = 2;
    String OPERATE_FAILED_MSG = "请勿频繁操作";
}
