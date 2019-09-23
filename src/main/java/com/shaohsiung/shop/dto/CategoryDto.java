package com.shaohsiung.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CategoryDto implements Serializable {
    private Long categoryId;
    private String name;
    private Date createTime;
}
