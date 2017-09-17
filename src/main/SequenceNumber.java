package main;

import java.math.BigInteger;
import java.util.OptionalInt;
import java.util.Random;

public class SequenceNumber {
    public static void main(String[] args)
    {
        long startTime=System.currentTimeMillis();   //获取开始时间
        int itemCount = 1000;
        getSequenceNumberByRandom(itemCount);//556ms
        //getSequenceNumberByFibonacci(itemCount);
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
    }

    /**
     * 随机数方法获得随机不连续的数组
     * @param count 数组长度
     */
    private static void getSequenceNumberByRandom(int count)
    {
        long[] result = new long[count];
        int interval = 5;
        int min = 0;
        int max = min+interval;
        for (int i=0;i<count;i++)
        {
            result[i] = getRandom(min,max);
            min = max;
            max = min+interval;
        }
        for (int i=0;i<count;i++)
        {
            System.out.println(String.format("Item%d:%s",i+1,result[i]));
        }
    }

    /**
     * 在(min,max)间取随机数（不含头尾数字）
     * @param min 头数字
     * @param max 尾数字
     * @return 随机数
     */
    private static long getRandom(int min, int max){
        if(max-min == 1) throw new IllegalArgumentException("最大数和最小数的间隔必须超过1");
        Random random = new Random();
        OptionalInt result= random.ints(min+1,max).findFirst();
        if(result.isPresent())return result.getAsInt();
        throw new NullPointerException("未生成随机数");
    }

    private static void getSequenceNumberByFibonacci(int count)
    {
        BigInteger[] result = new BigInteger[count];
        for (int i=4;i<count+4;i++)
        {
            result[i-4]= fibonacci(i);
        }
        for (int i=0;i<count;i++)
        {
            System.out.println(String.format("Item%d:%s",i+1,result[i]));
        }
    }
    /**
     * 斐波那契数列（非递归）
     * @param n 数字序号
     * @return bigInteger 防止溢出
     */
    private static BigInteger fibonacci(int n)
    {
        if(n<0)
        {
            throw new IllegalArgumentException("IllegalArgument");
        }
        if(n<=1)
        {
            return BigInteger.valueOf(n);
        }
        BigInteger prev2nd = BigInteger.valueOf(0);
        BigInteger prev =BigInteger.valueOf(1);
        BigInteger current = BigInteger.valueOf(0);
        for (int i =2;i<n+1;i++)
        {
            current =  prev2nd.add(prev);
            prev2nd = prev;
            prev = current;
        }
        return current;

    }
}
