package com.beyondhost.exam.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class OrgInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    public String OrgName;
    public double Longitude;
    public double Latitude;
    public String Address;
    public int CityId;

    public String Phone;
    public long PoiId;
    public int RoomCount;
    public Date OpenTime;

}
