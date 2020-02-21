package com.nokia.test.seatmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Floor {
    private int id;
    private int companyId;
    private List<Seat> seats;
}
