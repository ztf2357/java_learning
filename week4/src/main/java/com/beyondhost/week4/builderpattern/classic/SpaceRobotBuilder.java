package com.beyondhost.week4.builderpattern.classic;

public class SpaceRobotBuilder implements IRobotBuilder {

    private Robot robot;

    SpaceRobotBuilder()
    {
        robot = new Robot();
    }

    @Override
    public void buildHead() {
        robot.setHead("Spacerobot's head");
    }

    @Override
    public void buildBody() {
        robot.setBody("Spacerobot's body");
    }

    @Override
    public void buildFeet() {
        robot.setFeet("Spacerobot's feet");
    }

    @Override
    public Robot getProduct() {
        return robot;
    }
}
