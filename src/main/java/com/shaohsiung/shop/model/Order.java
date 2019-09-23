package com.shaohsiung.shop.model;

import com.shaohsiung.shop.dto.OrderItemDto;
import com.shaohsiung.shop.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private Long orderId;
    /** 订单流水号 */
    private String orderNo;
    private BigDecimal total;
    private OrderStatus status;
    private Long userId;

    private Date createTime;
}
