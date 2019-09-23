package com.shaohsiung.shop.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 购物车
 */
@Data
@Builder
public class Cart implements Serializable {
    private Long userId;
    /** 商品id， 商品项 */
    private Map<Long, CartItem> content;
    private BigDecimal total;
}
