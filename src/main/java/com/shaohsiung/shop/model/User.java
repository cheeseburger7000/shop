package com.shaohsiung.shop.model;

import com.shaohsiung.shop.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Long userId;
    private String userName;
    private String password;
    private String mobile;
    private UserStatus status;
}
