package com.shaohsiung.shop.model.enums;

import lombok.Getter;

/**
 * 订单状态
 * 0未发货 1取消中 2已发货 3已完成 4已关闭
 */
@Getter
public enum  OrderStatus {
    NOT_SHIPPED(0, "未发货"),
    CANCEL(1, "取消中"),
    SHIPPED(2, "已发货"),
    COMPLETED(3, "已完成"),
    CLOSED(4, "已关闭")
    ;

    private Integer code;
    private String message;

    OrderStatus (Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
