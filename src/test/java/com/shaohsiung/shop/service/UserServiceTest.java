package com.shaohsiung.shop.service;

import com.shaohsiung.shop.dto.UserDto;
import com.shaohsiung.shop.model.User;
import com.shaohsiung.shop.model.enums.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void register() {
        User user = User.builder().userName("john")
                .password("john")
                .mobile("13055228247")
                .status(UserStatus.NORMAL)
                .build();
        UserDto register = userService.register(user);
    }

    @Test
    public void login() {
        UserDto login = userService.login("shaohsiung", "shaohsiung");
    }

    @Test
    public  void  userList() {
        List<UserDto> userDtos = userService.userList(1, 2);
    }

    @Test
    public  void  updateUserStatus() {
        UserDto userDto = userService.updateUserStatus(5L, UserStatus.FREEZE);
    }
}