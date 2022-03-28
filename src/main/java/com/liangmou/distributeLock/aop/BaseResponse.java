package com.liangmou.distributeLock.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private int code = 0;
    private String msg = "Successful";

    public static BaseResponse addError(int code, String msg){
        return new BaseResponse(code, msg);
    }


    public static BaseResponse addResult(){
        return new BaseResponse();
    }


}
