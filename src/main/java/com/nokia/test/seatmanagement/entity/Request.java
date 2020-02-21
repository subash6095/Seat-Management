package com.nokia.test.seatmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private int id;
    private int raiserId;
    private int approverId;
    private RequestStatus requestStatus;
    private long createdTime;
    private long modifiedTime;
    private int requestedCompanyId;
    private int requestedFloorId;
    private int requestedSeatId;
}
