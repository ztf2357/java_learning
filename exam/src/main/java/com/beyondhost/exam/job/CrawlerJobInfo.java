package com.beyondhost.exam.job;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class CrawlerJobInfo {

    public Date getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(Date jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public Date getJobEndTime() {
        return jobEndTime;
    }

    public void setJobEndTime(Date jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

    private Date jobStartTime;
    private Date jobEndTime;
    private AtomicInteger orgInfoNum;
    private AtomicInteger roomTypeNum;

    public CrawlerJobInfo()
    {
        orgInfoNum = new AtomicInteger(0);
        roomTypeNum = new AtomicInteger(0);
    }

    public void increaseOrgInfoNum()
    {
        orgInfoNum.incrementAndGet();
    }

    public void increaseRoomTypeInfoNum(int num)
    {
        for(int i=0;i< num;i++) {
            roomTypeNum.incrementAndGet();
        }
    }

    public int getOrgInfoNum()
    {
        return orgInfoNum.get();
    }

    public int getRoomTypInfoNum()
    {
        return roomTypeNum.get();
    }

}
