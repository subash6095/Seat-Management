package com.nokia.test.seatmanagement.service.impl;

import com.nokia.test.seatmanagement.dao.UserDAO;
import com.nokia.test.seatmanagement.entity.Notification;
import com.nokia.test.seatmanagement.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotificationServiceImpl implements NotificationService {
    private AtomicInteger idGenerator = new AtomicInteger(1);

    @Autowired
    private UserDAO userDAO;

    @Override
    public void notifyMessage(int userId, Object object) {
        Notification notification = new Notification();
        notification.setId(idGenerator.getAndIncrement());
        notification.setObject(object);
        userDAO.getUser(userId).onNotify(notification);
    }
}
