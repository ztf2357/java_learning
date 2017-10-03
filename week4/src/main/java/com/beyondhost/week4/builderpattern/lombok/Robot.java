package com.beyondhost.week4.builderpattern.lombok;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Robot {
    private String head;
    private String body;
    private String feet;
}
