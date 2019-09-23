package com.shaohsiung.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderItemDto implements Serializable {
    private Long orderItemId;
    private Long goodsId;

    /** 商品名称 */
    private String name;
    /** 商品图片 */
    private String image;
    private String categoryName;

    /** 商品数量 */
    private Integer count;
    private BigDecimal amount;
}
