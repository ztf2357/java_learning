package com.beyondhost.week4;

import com.beyondhost.week4.builderpattern.pojo.Robot;
import org.junit.Assert;
import org.junit.Test;

public class POJOBuilderTest {

    @Test
    public void TestPOJOBuilder() {
        Robot robot = Robot.builder().buildHead("头")
                .buildBody("体").buildFeet("脚").build();
        Assert.assertEquals("体",robot.getBody());
    }
}
