package com.beyondhost.exam.util;

import java.util.OptionalInt;
import java.util.Random;

public class RandomHelper {

    public static long getRandom(int min, int max){
        if(max-min == 1) throw new IllegalArgumentException("最大数和最小数的间隔必须超过1");
        Random random = new Random();
        OptionalInt result= random.ints(min+1,max).findFirst();
        if(result.isPresent())return result.getAsInt();
        throw new NullPointerException("未生成随机数");
    }
}
