package com.nokia.test.seatmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPosition {
    private int floorId;
    private int seatId;
}
