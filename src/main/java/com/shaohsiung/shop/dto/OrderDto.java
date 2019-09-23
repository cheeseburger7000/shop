package com.shaohsiung.shop.dto;

import com.shaohsiung.shop.model.enums.OrderStatus;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDto implements Serializable {
    private Long orderId;
    /** 订单流水号 */
    private String orderNo;
    private BigDecimal total;
    private OrderStatus status;
    private Long userId;
    private Date createTime;
}
