package com.nokia.test.seatmanagement.service;

import com.nokia.test.seatmanagement.entity.RequestStatus;
import com.nokia.test.seatmanagement.entity.Response;
import com.nokia.test.seatmanagement.entity.Seat;
import com.nokia.test.seatmanagement.entity.dto.RequestDTO;

import java.util.List;

public interface SeatAllocationService {
    List<Seat> getAvailableSeats(int companyId, int floorId);

    Response requestSeatChange(RequestDTO requestDTO);

    void processSeatChangeRequest(int approverUserId, RequestStatus requestStatus);

    void doSeatChange(int userId, int companyId, int floorId, int seatId);
}
