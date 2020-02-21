package com.nokia.test.seatmanagement.entity.dto;

import com.nokia.test.seatmanagement.entity.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private int userId;
    private int companyId;
    private int requestedFloorId;
    private int requestedSeatId;
}
