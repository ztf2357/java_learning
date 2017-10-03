package com.beyondhost.week4;

import com.beyondhost.week4.builderpattern.lombok.Robot;
import org.junit.Assert;
import org.junit.Test;

public class LombokBuilderTest {

    @Test
    public void TestLombokBuilder()
    {
        com.beyondhost.week4.builderpattern.lombok.Robot robot = Robot.builder().head("头")
                .body("体").feet("脚").build();

        Assert.assertEquals("体",robot.getBody());
    }
}
