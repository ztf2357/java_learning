package com.beyondhost.exam.entity;

import lombok.Data;

import java.util.Date;

/**
 * 日志中打印内容
 */
@Data
public class LogInfo {

    public Date StartTime;

    public Date EndTime;

    public String Duration;

    public int OrgNum ;

    public int RoomTypeNum;

}
