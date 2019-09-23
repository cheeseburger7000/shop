package com.shaohsiung.shop.dto;

import com.shaohsiung.shop.model.enums.GoodsStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车项
 */
@Data
@Builder
public class CartItem implements Serializable {
    private Long goodsId;
    private String name;
    private Integer count;
    private BigDecimal amount;
    /** 商品状态 */
    private GoodsStatus status;
}
