package com.shaohsiung.shop.service;

import com.shaohsiung.shop.dto.Cart;
import com.shaohsiung.shop.error.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTest {
    @Autowired
    private CartService cartService;

    @Test
    public void getCart() {
        Cart cart = cartService.getCart(4L);
    }

    @Test
    public void addGoods2Cart() {
//        Cart cart = cartService.addGoods2Cart(8L, 4L);

        cartService.addGoods2Cart(9L, 4L);
        cartService.addGoods2Cart(9L, 4L);
        cartService.addGoods2Cart(10L, 4L);
        cartService.addGoods2Cart(10L, 4L);
        Cart cart = cartService.addGoods2Cart(10L, 4L);
    }

//    @Test(expected = ServiceException.class)
    @Test
    public void deleteGoodsFromCart() {
        Cart cart = cartService.deleteGoodsFromCart(9L, 5L);
    }

    @Test(expected = ServiceException.class)
    public void decreaseGoodsFromCart() {
        Cart cart = cartService.decreaseGoodsFromCart(8L, 5L);
    }

    @Test
    public void clearCart() {
        Cart cart = cartService.clearCart(5L);
    }
}