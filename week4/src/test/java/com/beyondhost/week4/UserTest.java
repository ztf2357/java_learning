package com.beyondhost.week4;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {

    @Test
    public void testUser()
    {
        User user = new User(100);
        user.Email = "ztf@qq.com";
        user.Name = "ztf";
        user.Gender = false;
        user.Mobile ="188888888";

        Assert.assertEquals("User(Id=100)",user.toString());
    }
}
