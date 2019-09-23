package com.shaohsiung.shop;

import com.shaohsiung.shop.api.request.Response;
import com.shaohsiung.shop.model.enums.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class EnumTest {
    @Test
    public void testEnum() {
        UserEnum userEnum = new UserEnum();
        log.info("{}", userEnum.userStatus);
    }

    @Test
    public void testData() {
    }
}

class UserEnum {
    public UserStatus userStatus = UserStatus.NORMAL;
}