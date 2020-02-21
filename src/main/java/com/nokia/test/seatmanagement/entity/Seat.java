package com.nokia.test.seatmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Seat {
    private int id;
    private int floorId;
    private SeatStatus seatStatus;
}
