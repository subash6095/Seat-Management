package com.nokia.test.seatmanagement.dao;

import com.nokia.test.seatmanagement.entity.UserPosition;
import com.nokia.test.seatmanagement.exception.SeatAllocationException;
import com.nokia.test.seatmanagement.entity.Role;
import com.nokia.test.seatmanagement.entity.User;
import com.nokia.test.seatmanagement.util.ERROR_CODE;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDAO {
    private final List<User> users = new ArrayList<>();

    @PostConstruct
    public void loadUsers() {
        users.add(new User(1, "Subash", 2, 1001, new UserPosition(1, 1), Role.USER));
        users.add(new User(2, "Prem", 0, 1001, new UserPosition(1, 2), Role.ADMIN));
        users.add(new User(3, "Mani", 2, 1001, new UserPosition(2, 1), Role.USER));
    }

    public User getUser(int userId) {
        Optional<User> userOptional = users.stream().filter(user -> user.getId() == userId).findAny();
        if (userOptional.isPresent())
            return userOptional.get();
        throw new SeatAllocationException(ERROR_CODE.EC_001.getErrorMessage());
    }

}
