package com.beyondhost.week4.builderpattern.classic;

public class Director {

    private IRobotBuilder _builder;

    Director(IRobotBuilder builder)
    {
        _builder = builder;
    }

    void buildRobot()
    {
        _builder.buildHead();
        _builder.buildBody();
        _builder.buildFeet();
    }
}
