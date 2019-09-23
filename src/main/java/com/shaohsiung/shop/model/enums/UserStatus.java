package com.shaohsiung.shop.model.enums;

import lombok.Getter;

/**
 * 用户状态
 * 0未激活 1正常 2冻结
 */
@Getter
public enum  UserStatus {
    INACTIVATED(0, "未激活"),
    NORMAL(1, "正常"),
    FREEZE(2, "冻结")
    ;

    private Integer code;
    private String message;

    UserStatus (Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
