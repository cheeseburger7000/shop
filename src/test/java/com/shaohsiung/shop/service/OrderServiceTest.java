package com.shaohsiung.shop.service;

import com.shaohsiung.shop.dto.OrderDetailsDto;
import com.shaohsiung.shop.dto.OrderDto;
import com.shaohsiung.shop.error.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test(expected = ServiceException.class)
//    @Test
    public void createOrder() {
        OrderDto order = orderService.createOrder(4L);
    }

//    @Test(expected = ServiceException.class)
    //@Transactional
    @Test
    public void cancelOrder() {
        OrderDto orderDto = orderService.cancelOrder(4L, 4L);
    }

    @Test
    public void getOrderDetailsDtoByOrderId() {
        OrderDetailsDto orderDetailsDto = orderService.getOrderDetailsDtoByOrderId("11570432-c2b6-4bc9-9e7f-d21b4dcdd813");
    }

    @Test
    public void getUserOrderList() {
        List<OrderDto> userOrderList = orderService.getUserOrderList(4L, 0, 2);
    }

    @Test
    public void ship() {
        OrderDto ship = orderService.ship(4L, 4L);
    }

    @Test
    public void receipt() {
        OrderDto receipt = orderService.receipt(4L, 4L);
    }

    @Test
    public void cancel() {
        OrderDto cancel = orderService.cancel(4L, 4L);
    }

    @Test
    public void getOrderList() {
        List<OrderDto> orderList = orderService.getOrderList(0, 4);
    }
}