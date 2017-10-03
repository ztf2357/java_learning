package com.beyondhost.week4;

import org.junit.Assert;
import org.junit.Test;

public class EnumSingletonTest {

    @Test
    public void testEnumSingleton()
    {
        EnumSingleton instance1 = EnumSingleton.getInstance();
        EnumSingleton instance2 = EnumSingleton.getInstance();
        Assert.assertEquals(instance1,instance2);
    }
}
