package main;

import java.math.BigInteger;

public class SequenceNumber {
    public static void main(String[] args)
    {
        int itemCount = 1003;
        BigInteger[] result = new BigInteger[itemCount];
        for (int i=0;i<itemCount;i++)
        {
            result[i]= Fibonacci(i);
        }
        //从第三项开始输出
        for (int i=3;i<itemCount;i++)
        {
            System.out.println(String.format("Item%d:%s",(i-2),result[i]));
        }
    }

    /**
     * 斐波那契数列（非递归）
     * @param n 数字序号
     * @return bigInteger 防止溢出
     */
    private static BigInteger Fibonacci(int n)
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
