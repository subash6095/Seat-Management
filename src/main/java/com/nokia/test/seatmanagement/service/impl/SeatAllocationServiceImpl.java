package com.nokia.test.seatmanagement.service.impl;

import com.nokia.test.seatmanagement.dao.CompanyDAO;
import com.nokia.test.seatmanagement.dao.RequestDAO;
import com.nokia.test.seatmanagement.dao.UserDAO;
import com.nokia.test.seatmanagement.entity.*;
import com.nokia.test.seatmanagement.entity.dto.RequestDTO;
import com.nokia.test.seatmanagement.exception.SeatAllocationException;
import com.nokia.test.seatmanagement.service.NotificationService;
import com.nokia.test.seatmanagement.service.SeatAllocationService;
import com.nokia.test.seatmanagement.util.ERROR_CODE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SeatAllocationServiceImpl implements SeatAllocationService {
    private AtomicInteger idGenerator = new AtomicInteger(1);
    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RequestDAO requestDAO;

    @Autowired
    private NotificationService notificationService;

    @Override
    public List<Seat> getAvailableSeats(int companyId, int floorId) {
        return companyDAO.getAvailableSeats(companyId, floorId);
    }

    @Override
    public Response requestSeatChange(RequestDTO requestDTO) {
        User user = userDAO.getUser(requestDTO.getUserId());
        if (!companyDAO.isSeatAvailable(requestDTO.getCompanyId(), requestDTO.getRequestedFloorId(), requestDTO.getRequestedSeatId())) {
            throw new SeatAllocationException(ERROR_CODE.EC_006.getErrorMessage());
        }
        Request request = new Request();
        request.setId(idGenerator.getAndIncrement());
        request.setRaiserId(user.getId());
        request.setRequestStatus(RequestStatus.PENDING);
        request.setCreatedTime(System.currentTimeMillis());
        request.setModifiedTime(System.currentTimeMillis());
        request.setApproverId(user.getAdminId());
        request.setRequestedCompanyId(requestDTO.getCompanyId());
        request.setRequestedFloorId(requestDTO.getRequestedFloorId());
        request.setRequestedSeatId(requestDTO.getRequestedSeatId());
        companyDAO.updateSeatStatus(requestDTO.getCompanyId(), requestDTO.getRequestedFloorId(), requestDTO.getRequestedSeatId(), SeatStatus.CLAIMED);
        requestDAO.addRequest(request);

        return new Response(idGenerator.get(), request.getId(), request.getRequestStatus());
    }

    @Override
    public void processSeatChangeRequest(int approverUserId, RequestStatus requestStatus) {
        List<Request> requests = requestDAO.getRequestRaisedToAdmin(approverUserId);
        requests.forEach(request -> {
            if (requestStatus == RequestStatus.ACCEPTED) {
                companyDAO.updateSeatStatus(request.getRequestedCompanyId(), request.getRequestedFloorId(), request.getRequestedSeatId(), SeatStatus.UNAVAILABLE);
                userDAO.getUser(request.getRaiserId()).setPosition(new UserPosition(request.getRequestedFloorId(), request.getRequestedSeatId()));
            } else
                companyDAO.updateSeatStatus(request.getRequestedCompanyId(), request.getRequestedFloorId(), request.getRequestedSeatId(), SeatStatus.AVAILABLE);
            requestDAO.updateStatus(request.getId(), requestStatus);
        });
    }

    @Override
    public void doSeatChange(int userId, int companyId, int floorId, int seatId) {
        User user = userDAO.getUser(userId);
        if (!companyDAO.isSeatAvailable(companyId, floorId, seatId)) {
            throw new SeatAllocationException(ERROR_CODE.EC_006.getErrorMessage());
        }

        companyDAO.updateSeatStatus(companyId, user.getPosition().getFloorId(), user.getPosition().getFloorId(), SeatStatus.AVAILABLE);
        companyDAO.updateSeatStatus(companyId, floorId, seatId, SeatStatus.UNAVAILABLE);

        userDAO.getUser(userId).setPosition(new UserPosition(floorId, seatId));
        notificationService.notifyMessage(userId, userDAO.getUser(userId).getPosition());
    }
}
