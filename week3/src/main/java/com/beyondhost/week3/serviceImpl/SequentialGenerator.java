package com.beyondhost.week3.serviceImpl;
import com.beyondhost.week3.service.ISequentialGenerator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.Random;

@Component
public class SequentialGenerator implements ISequentialGenerator {

    @Override
    public int[] sequentialGenerator(int numberCount) {
        return getSequenceNumberByRandom(numberCount);
    }


    /**
     * 随机数方法获得随机不连续的数组
     * @param count 数组长度
     */
    private static int[] getSequenceNumberByRandom(int count)
    {
        int[] result = new int[count];
        int interval = 5;
        int min = 0;
        int max = min+interval;
        for (int i=0;i<count;i++)
        {
            result[i] = getRandom(min,max);
            min = max;
            max = min+interval;
        }
        return result;
    }

    /**
     * 在(min,max)间取随机数（不含头尾数字）
     * @param min 头数字
     * @param max 尾数字
     * @return 随机数
     */
    private static int getRandom(int min, int max){
        if(max-min == 1) throw new IllegalArgumentException("最大数和最小数的间隔必须超过1");
        Random random = new Random();
        OptionalInt result= random.ints(min+1,max).findFirst();
        if(result.isPresent())return result.getAsInt();
        throw new NullPointerException("未生成随机数");
    }
}
