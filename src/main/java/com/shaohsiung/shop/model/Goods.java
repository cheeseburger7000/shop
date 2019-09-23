package com.shaohsiung.shop.model;

import com.shaohsiung.shop.model.enums.GoodsStatus;
import com.shaohsiung.shop.model.enums.UserStatus;
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
public class Goods implements Serializable {
    private Long goodsId;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private GoodsStatus status;
    private Date createTime;
    private String image;
    private Long categoryId;
}
