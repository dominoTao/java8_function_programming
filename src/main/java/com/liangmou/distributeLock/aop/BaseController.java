package com.liangmou.distributeLock.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BaseController {


    @PostMapping("post-test")
    @DistributeLock(key = "post_test_#{baseRequest.channel}", timeout = 10)
    public String postTest(@RequestBody String request){
        log.info(JSON.toJSONString(request));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(BaseResponse.addResult());
    }
}
