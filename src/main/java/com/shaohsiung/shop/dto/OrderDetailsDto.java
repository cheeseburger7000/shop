package com.shaohsiung.shop.dto;

import com.shaohsiung.shop.model.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDetailsDto {
    private Long orderId;
    /** 订单流水号 */
    private String orderNo;
    private BigDecimal total;
    private OrderStatus status;
    private Long userId;
    private Date createTime;

    private List<OrderItemDto> orderItemDtoList;
}
