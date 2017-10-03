package com.beyondhost.week4;

import org.junit.Assert;
import org.junit.Test;

public class DoubleCheckSingletonTest {

    @Test
    public void testDoubleCheckSingleton()
    {
        DoubleCheckSingleton instance1 = DoubleCheckSingleton.getInstance();
        DoubleCheckSingleton instance2 = DoubleCheckSingleton.getInstance();
        Assert.assertEquals(instance1,instance2);
    }
}
