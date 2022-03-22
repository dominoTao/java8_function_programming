package com.liangmou.functional.ch1;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.liangmou.functional.ch1.bean.RespD;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunWith(JUnit4.class)
public class SerializableDemo {
    @Test
    public void run(){

        String sources = "";
        final Map<String, Map<String, RespD>> stringMapMap = JSON.parseObject(sources, new TypeReference<Map<String, Map<String, RespD>>>() {
        });
        Optional<Map<String, Map<String, RespD>>> theObj = Optional.ofNullable(stringMapMap);
        final Map<String, Map<String, RespD>> stringMapMap1 = theObj.get();
        final List<String> keyList = new ArrayList<>(stringMapMap1.keySet());
        final List<Map<String, RespD>> value = new ArrayList<>(stringMapMap1.values());
        value.forEach(t -> {
            final long count = t.values().stream().map(RespD::getB).filter(d -> d < 5).count();
            System.out.println("次数："+count);
        });
//        System.out.println(JSON.toJSONString(collect));
//        System.out.println(JSON.toJSONString(value));
    }
}
