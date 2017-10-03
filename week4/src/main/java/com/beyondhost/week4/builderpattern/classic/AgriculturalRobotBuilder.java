package com.beyondhost.week4.builderpattern.classic;

public class AgriculturalRobotBuilder implements IRobotBuilder {
    private Robot robot;

    AgriculturalRobotBuilder()
    {
        robot = new Robot();
    }

    @Override
    public void buildHead() {
        robot.setHead("AgriculturalRobot's head");
    }

    @Override
    public void buildBody() {
        robot.setBody("AgriculturalRobot's body");
    }

    @Override
    public void buildFeet() {
        robot.setFeet("AgriculturalRobot's feet");
    }

    @Override
    public Robot getProduct() {
        return robot;
    }
}
