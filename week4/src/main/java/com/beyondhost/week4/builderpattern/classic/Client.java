package com.beyondhost.week4.builderpattern.classic;

public class Client {
    public static void main(String[] args)
    {
        SpaceRobotBuilder spaceRobotBuilder = new SpaceRobotBuilder();
        AgriculturalRobotBuilder agriculturalRobotBuilder = new AgriculturalRobotBuilder();
        Director director = new Director(spaceRobotBuilder);
        director.buildRobot();
        Robot spaceRobot = spaceRobotBuilder.getProduct();

        director = new Director(agriculturalRobotBuilder);
        director.buildRobot();
        Robot agriculturalRobot = agriculturalRobotBuilder.getProduct();
        System.out.println(spaceRobot.toString());
        System.out.println(agriculturalRobot.toString());
    }
}
