package com.nokia.test.seatmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private String name;
    private int adminId;
    private int companyId;
    private UserPosition position;
    private Role role;

    public void onNotify(Notification notification) {
        System.out.println(" Notifying User  : " + id + " - ROLE : " + role +
                " - Message " + notification.getObject());
    }
}
