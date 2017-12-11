package com.beyondhost.exam.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomTypeInfo {

    public long RoomId;
    public long PartnerId;
    public long PoiId;
    public String RoomName;
    public int OriginalPrice;
}
