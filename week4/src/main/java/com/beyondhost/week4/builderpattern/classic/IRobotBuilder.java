package com.beyondhost.week4.builderpattern.classic;

public interface IRobotBuilder
{
    void buildHead();
    void buildBody();
    void buildFeet();

    Robot getProduct();
}