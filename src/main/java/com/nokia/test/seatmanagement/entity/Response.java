package com.nokia.test.seatmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private int id;
    private int requestId;
    private RequestStatus requestStatus;
}
