package com.shaohsiung.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable {
    private Long orderItemId;
    private Long orderId;
    /** 商品名称  没有必要设置这个字段，CartItem是一个DTO，所以才设置这个字段。
    private String name;*/
    private Long goodsId;
    /** 商品数量 */
    private Integer count;
    private BigDecimal amount;
}
