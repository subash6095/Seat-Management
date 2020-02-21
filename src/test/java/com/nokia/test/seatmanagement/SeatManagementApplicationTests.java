package com.nokia.test.seatmanagement;

import com.nokia.test.seatmanagement.dao.CompanyDAO;
import com.nokia.test.seatmanagement.dao.RequestDAO;
import com.nokia.test.seatmanagement.dao.UserDAO;
import com.nokia.test.seatmanagement.entity.*;
import com.nokia.test.seatmanagement.entity.dto.RequestDTO;
import com.nokia.test.seatmanagement.service.SeatAllocationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class SeatManagementApplicationTests {

    @Autowired
    private SeatAllocationService seatAllocationService;

    @Autowired
    private RequestDAO requestDAO;

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private UserDAO userDAO;

    @Test
    void testUserRaisesSeatChangeRequestToAdmin() {
        System.out.println("User raised seat change request to management ::");
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setUserId(1);
        requestDTO.setCompanyId(1001);
        requestDTO.setRequestedFloorId(1);
        requestDTO.setRequestedSeatId(3);
        Response response = seatAllocationService.requestSeatChange(requestDTO);

        Request raisedReq = requestDAO.getRequest(response.getRequestId());

        Assertions.assertNotNull(raisedReq);
        Assertions.assertEquals(RequestStatus.PENDING, raisedReq.getRequestStatus());

        Seat seat = companyDAO.getSeat(requestDTO.getCompanyId(), requestDTO.getRequestedFloorId(), requestDTO.getRequestedSeatId());
        Assertions.assertNotNull(seat);
        Assertions.assertEquals(SeatStatus.CLAIMED, seat.getSeatStatus());

        seatAllocationService.processSeatChangeRequest(2, RequestStatus.ACCEPTED);

        Request afterRaisedReq = requestDAO.getRequest(response.getRequestId());

        Assertions.assertNotNull(afterRaisedReq);
        Assertions.assertEquals(RequestStatus.ACCEPTED, afterRaisedReq.getRequestStatus());

        Seat afterSeatChange = companyDAO.getSeat(requestDTO.getCompanyId(), requestDTO.getRequestedFloorId(), requestDTO.getRequestedSeatId());
        Assertions.assertNotNull(afterSeatChange);
        Assertions.assertEquals(SeatStatus.UNAVAILABLE, afterSeatChange.getSeatStatus());
    }

    @Test
    public void adminChangesUserSeatPosition() {
        int userId = 3;
        int companyId = 1001;
        int floorId = 2;
        int seatId = 1;

        //Before
        Seat beforeSeat = companyDAO.getSeat(companyId, floorId, seatId);
        Assertions.assertNotNull(beforeSeat);
        Assertions.assertEquals(SeatStatus.AVAILABLE, beforeSeat.getSeatStatus());

        seatAllocationService.doSeatChange(userId, companyId, floorId, seatId);

        //After
        Seat afterSeat = companyDAO.getSeat(companyId, floorId, seatId);
        Assertions.assertNotNull(afterSeat);
        Assertions.assertEquals(SeatStatus.UNAVAILABLE, afterSeat.getSeatStatus());

        UserPosition position = userDAO.getUser(userId).getPosition();
        Assertions.assertNotNull(position);
        Assertions.assertEquals(position.getFloorId(), floorId);
        Assertions.assertEquals(position.getSeatId(), seatId);
    }
}
