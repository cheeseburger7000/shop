package com.shaohsiung.shop.service;

import com.shaohsiung.shop.dto.GoodsDto;
import com.shaohsiung.shop.model.Goods;
import com.shaohsiung.shop.model.enums.GoodsStatus;
import com.shaohsiung.shop.model.enums.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceTest {
    @Autowired
    private GoodsService goodsService;

    @Test
    public void addGoods() {
        Goods goods = Goods.builder().categoryId(1L)
                .createTime(new Date())
                .name("测试商品")
                .price(new BigDecimal(12.00))
                .status(GoodsStatus.HAS_BEEN_REMOVED)
                .stock(12)
                .image("http://xxx.jpg")
                .build();
        GoodsDto added = goodsService.addGoods(goods);
    }

    @Test
    public void goodsListByCategoryId() {
        List<GoodsDto> goodsDtoList = goodsService.goodsListByCategoryId(0L, 0, 2);
    }

    @Test
    public void goodsList() {
        List<GoodsDto> page1 = goodsService.goodsList(0, 5);
        List<GoodsDto> page2 = goodsService.goodsList(1, 5);
        List<GoodsDto> page3 = goodsService.goodsList(2, 5);
    }

    @Test
    public void getById() {
        GoodsDto goodsDto = goodsService.getById(10L);
    }

    @Test
    public void search() {
        List<GoodsDto> a = goodsService.search("a", 0, 2);
    }

    @Test
    public void updateGoodsStock() {
        GoodsDto goodsDto = goodsService.updateGoodsStock(8L, 100);
        log.info("{}", goodsDto);
    }

    @Test
    public void updateGoodsCategory() {
        GoodsDto goodsDto = goodsService.updateGoodsCategory(8L, 1L);
    }

    @Test
    public void updateGoodsStatus() {
        GoodsDto goodsDto = goodsService.updateGoodsStatus(8L);
    }
}