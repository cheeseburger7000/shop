package com.shaohsiung.shop.api.request;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(0, "操作成功"),
    FAILURE(1, "操作失败")
    ;

    private Integer code;
    private String message;

    ResultCode (Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
