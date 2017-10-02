package com.beyondhost.week3.controller;

import com.beyondhost.week3.service.ISequentialGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class IndexController {

    private final ISequentialGenerator sequentialGenerator;

    @Autowired
    public IndexController(ISequentialGenerator sequentialGenerator) {
        this.sequentialGenerator = sequentialGenerator;
    }

    @GetMapping(value = "/", produces="application/json;charset=UTF-8")
    public String Index() {
        int NUMBER_COUNT = 1000;
        long startTimeMillis=System.currentTimeMillis();   //获取开始时间
        int[] sequentialNumbers = sequentialGenerator.sequentialGenerator(NUMBER_COUNT);
        long endTimeMillis=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(startTimeMillis-endTimeMillis)+"ms");
        Map<String,Object> result = new LinkedHashMap<>();
        result.put("StartTime",startTimeMillis);
        result.put("EndTime",endTimeMillis);
        result.put("ExecutionTime",endTimeMillis - startTimeMillis);
        result.put("Result",Arrays.toString(sequentialNumbers));
        return JSON.toJSONString(result);
    }
}

