package com.shaohsiung.shop.dto;

import com.shaohsiung.shop.model.enums.UserStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private Long userId;
    private String userName;
    private String mobile;
    private UserStatus status;
}
