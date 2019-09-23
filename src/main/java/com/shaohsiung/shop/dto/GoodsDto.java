package com.shaohsiung.shop.dto;

import com.shaohsiung.shop.model.enums.GoodsStatus;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoodsDto implements Serializable {
    private Long goodsId;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private GoodsStatus status;
    private Date createTime;
    private String image;
    private Long categoryId;
    private String categoryName;
}
