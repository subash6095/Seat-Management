package com.nokia.test.seatmanagement.dao;

import com.nokia.test.seatmanagement.entity.Request;
import com.nokia.test.seatmanagement.entity.RequestStatus;
import com.nokia.test.seatmanagement.exception.SeatAllocationException;
import com.nokia.test.seatmanagement.service.NotificationService;
import com.nokia.test.seatmanagement.util.ERROR_CODE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestDAO {
    private final List<Request> requests = new ArrayList<>();

    @Autowired
    private NotificationService notificationService;

    public Request getRequest(int requestId) {
        Optional<Request> req = requests.stream().filter(request -> request.getId() == requestId).findFirst();
        if(req.isPresent()) {
            return req.get();
        }
        throw new SeatAllocationException(ERROR_CODE.EC_005.getErrorMessage());
    }

    public void updateStatus(int requestId, RequestStatus requestStatus) {
        Request request = getRequest(requestId);
        request.setRequestStatus(requestStatus);
        request.setModifiedTime(System.currentTimeMillis());
        notificationService.notifyMessage(request.getRaiserId(), request);
    }

    public Request getRequestRaisedByUser(int requestId) {
        return null;
    }

    public List<Request> getRequestRaisedToAdmin(int adminId) {
        List<Request> requestList = requests.stream().filter(request -> request.getApproverId() == adminId)
                .collect(Collectors.toList());
        return requestList;
    }

    public void addRequest(Request request) {
        requests.add(request);
        notificationService.notifyMessage(request.getApproverId(), request);
    }
}
