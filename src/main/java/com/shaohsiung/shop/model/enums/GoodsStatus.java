package com.shaohsiung.shop.model.enums;

import lombok.Getter;

/**
 * 商品状态
 * 0上架中 1已下架
 */
@Getter
public enum GoodsStatus {
    ON_THE_SHELF(0, "上架中"),
    HAS_BEEN_REMOVED(1, "已下架")
    ;

    private Integer code;
    private String message;

    GoodsStatus (Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
