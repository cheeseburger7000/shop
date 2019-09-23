package com.shaohsiung.shop.service;

import com.shaohsiung.shop.dto.*;
import com.shaohsiung.shop.error.ServiceException;
import com.shaohsiung.shop.mapper.GoodsMapper;
import com.shaohsiung.shop.mapper.OrderMapper;
import com.shaohsiung.shop.model.Order;
import com.shaohsiung.shop.model.OrderItem;
import com.shaohsiung.shop.model.enums.OrderStatus;
import com.shaohsiung.shop.util.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 订单模块
 */
@Slf4j
@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 创建订单
     * @param userId
     * @return
     */
    public OrderDto createOrder(Long userId) {
        OrderDto result = new OrderDto();

        // 获取用户购物车，购物车为空时，否则抛出异常
        Cart cart = cartService.getCart(userId);
        if (! (cart.getContent().size() > 0)) {
            log.error("购物车没有任何商品，无法创建订单 userId:{}", userId);
            throw new ServiceException("购物车没有任何商品，无法创建订单");
        }

        // 创建订单对象，持久化到数据库中，获取主键
        String orderNo = AppUtils.nextOrderNo();
        Order order = Order.builder().orderNo(orderNo)
                .status(OrderStatus.NOT_SHIPPED)
                .userId(userId)
                .total(cart.getTotal())
                .createTime(new Date())
                .build();
        int savedOrder = orderMapper.save(order);
        if (!(savedOrder == 1)) {
            log.error("【订单模块】订单创建失败 userId: {}", userId);
            throw new ServiceException("订单创建失败");
        }
        log.info("【订单模块】创建订单 order: {}", order);

        // 根据cartitem创建orderitem
        Collection<CartItem> cartItems = cart.getContent().values();
        cartItems.forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            BeanUtils.copyProperties(cartItem, orderItem);
            orderItem.setOrderId(order.getOrderId());
            int savedOrderItem = orderMapper.saveOrderItem(orderItem);
            if (!(savedOrder == 1)) {
                log.error("【订单模块】订单创建失败 userId: {}", userId);
                throw new ServiceException("订单创建失败");
            }
            log.info("【订单模块】创建订单项 order: {}", orderItem);

            GoodsDto goodsDto = goodsService.getById(cartItem.getGoodsId());
            // TODO 判断商品库存，操作商品库存
        });
        // 创建订单DTO
        BeanUtils.copyProperties(order, result);

        // 清空购物车
        cartService.clearCart(userId);
        return result;
    }

    /**
     * 取消订单
     *
     * 两种情况
     *  1. 订单未发货：退款，订单直接关闭
     *  2. 订单已发货：取消中，后台管理员确认之后，订单关闭
     * @param userId
     * @param orderId
     * @return
     */
    public OrderDto cancelOrder(Long userId, Long orderId) {
        // 根据userId和orderId获取订单
        OrderStatus orderStatus = orderMapper.getOrderStatusByUserIdAndOrderId(userId, orderId);
        // 判断订单状态，根据不同的状态进行处理
        if (orderStatus.equals(OrderStatus.NOT_SHIPPED)) {
            // 订单未发货
            int updated = orderMapper.updateOrderStatusByOrderId(OrderStatus.CLOSED, orderId);
            if (!(updated == 1)) {
                log.error("【订单模块】订单取消失败 userId: {}，orderId：{}", userId, orderId);
                throw new ServiceException("订单取消失败");
            }
        } else if (orderStatus.equals(OrderStatus.SHIPPED)) {
            // 订单已发货
            int updated = orderMapper.updateOrderStatusByOrderId(OrderStatus.CANCEL, orderId);
            if (!(updated == 1)) {
                log.error("【订单模块】订单取消失败 userId: {}，orderId：{}", userId, orderId);
                throw new ServiceException("订单取消失败");
            }
        } else if (orderStatus.equals(OrderStatus.CANCEL)){
            log.error("【订单模块】该订单正在取消中，请等待管理员关闭 userId: {}，orderId：{}", userId, orderId);
            throw new ServiceException("该订单正在取消中，请等待管理员关闭");
        } else if (orderStatus.equals(OrderStatus.COMPLETED)){
            log.error("【订单模块】该订单已完成，无法取消 userId: {}，orderId：{}", userId, orderId);
            throw new ServiceException("该订单已完成，无法取消");
        } else if (orderStatus.equals(OrderStatus.CLOSED)){
            log.error("【订单模块】该订单已关闭 userId: {}，orderId：{}", userId, orderId);
            throw new ServiceException("该订单已关闭");
        } else {
            log.error("【订单模块】订单取消失败 userId: {}，orderId：{}", userId, orderId);
            throw new ServiceException("订单取消失败");
        }

        return createOrderDtoByOrderId(orderId);
    }

    /**
     * 根据订单号获取orderDetailsDto
     * @param orderNo
     * @return
     */
    public OrderDetailsDto getOrderDetailsDtoByOrderId(String orderNo) {
        OrderDetailsDto result = new OrderDetailsDto();

        Order order = orderMapper.getByOrderNo(orderNo);
        BeanUtils.copyProperties(order, result);

        // 操作orderItem
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        List<OrderItem> orderItemsList = orderMapper.getOrderItemListByOrderId(order.getOrderId());
        orderItemsList.forEach(orderItem -> {
            OrderItemDto orderItemDto = new OrderItemDto();
            BeanUtils.copyProperties(orderItem, orderItemDto);

            GoodsDto goodsDto = goodsService.getById(orderItem.getGoodsId());
            orderItemDto.setName(goodsDto.getName());
            orderItemDto.setCategoryName(goodsDto.getCategoryName());
            orderItemDto.setImage(goodsDto.getImage());

            orderItemDtoList.add(orderItemDto);
        });
        result.setOrderItemDtoList(orderItemDtoList);
        return result;
    }

    /**
     * 根据用户id查询订单列表
     * @param userId
     * @return
     */
    public List<OrderDto> getUserOrderList(Long userId, int pageNum, int pageSize) {
        List<OrderDto> result = new ArrayList<>();
        List<Order> orderList = orderMapper.getOrderListByUserId(userId, new RowBounds(pageNum, pageSize));
        orderList.forEach(order -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);

            result.add(orderDto);
        });
        return result;
    }

    /**
     * 发货
     * 后台
     * @param orderId
     * @return
     */
    public OrderDto ship(Long userId, Long orderId) {
        OrderStatus orderStatus = orderMapper.getOrderStatusByUserIdAndOrderId(userId, orderId);
        if (orderStatus.equals(OrderStatus.NOT_SHIPPED)) {
            // 发货
            int updated = orderMapper.updateOrderStatusByOrderId(OrderStatus.SHIPPED, orderId);
            if (!(updated == 1)) {
                log.error("【订单模块】发货失败 userId: {}, orderId: {}", userId, orderId);
                throw new ServiceException("发货失败");
            }
        } else if (orderStatus.equals(OrderStatus.SHIPPED)) {
            log.error("【订单模块】该订单已发货 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("该订单已发货");
        } else if (orderStatus.equals(OrderStatus.CANCEL)) {
            log.error("【订单模块】该订单正在取消中 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("该订单正在取消中");
        } else if (orderStatus.equals(OrderStatus.COMPLETED)) {
            log.error("【订单模块】该订单已完成 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("该订单已完成");
        } else if (orderStatus.equals(OrderStatus.CLOSED)) {
            log.error("【订单模块】该订单已关闭 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("该订单已关闭");
        } else {
            log.error("【订单模块】发货失败 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("发货失败");
        }
        return createOrderDtoByOrderId(orderId);
    }

    /**
     * 收货
     * 前台
     * @param userId
     * @param orderId
     * @return
     */
    public OrderDto receipt(Long userId, Long orderId) {
        OrderStatus orderStatus = orderMapper.getOrderStatusByUserIdAndOrderId(userId, orderId);
        if (orderStatus.equals(OrderStatus.NOT_SHIPPED)) {
            log.error("【订单模块】该订单未发货 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("该订单未发货");
        } else if (orderStatus.equals(OrderStatus.SHIPPED)) {
            // 收货
            int updated = orderMapper.updateOrderStatusByOrderId(OrderStatus.COMPLETED, orderId);
            if (!(updated == 1)) {
                log.error("【订单模块】收货失败 userId: {}, orderId: {}", userId, orderId);
                throw new ServiceException("收货失败");
            }
        } else if (orderStatus.equals(OrderStatus.CANCEL)) {
            log.error("【订单模块】该订单正在取消中 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("该订单正在取消中");
        } else if (orderStatus.equals(OrderStatus.COMPLETED)) {
            log.error("【订单模块】该订单已完成 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("该订单已完成");
        } else if (orderStatus.equals(OrderStatus.CLOSED)) {
            log.error("【订单模块】该订单已关闭 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("该订单已关闭");
        } else {
            log.error("【订单模块】发货失败 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("发货失败");
        }
        return createOrderDtoByOrderId(orderId);
    }

    /**
     * 管理员确认取消订单
     * 后台
     * @param userId
     * @param orderId
     * @return
     */
    public OrderDto cancel(Long userId, Long orderId) {
        OrderStatus orderStatus = orderMapper.getOrderStatusByUserIdAndOrderId(userId, orderId);
        if (!orderStatus.equals(OrderStatus.CANCEL)) {
            log.error("【订单模块】订单取消失败 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("订单取消失败");
        }

        int updated = orderMapper.updateOrderStatusByOrderId(OrderStatus.CLOSED, orderId);
        if (!(updated == 1)) {
            log.error("【订单模块】订单取消失败 userId: {}, orderId: {}", userId, orderId);
            throw new ServiceException("订单取消失败");
        }
        log.info("【订单模块】管理员取消订单 orderId: {}, userId: {}", orderId, userId);
        return createOrderDtoByOrderId(orderId);
    }

    /**
     * 订单列表
     * 后台
     *
     * 功能说明：获取所有用户订单，按订单状态升序排列
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<OrderDto> getOrderList(int pageNum, int pageSize) {
        List<OrderDto> result = new ArrayList<>();

        List<Order> orderList = orderMapper.getOrderList(new RowBounds(pageNum, pageSize));
        orderList.forEach(order -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);

            result.add(orderDto);
        });
        return result;
    }

    /**
     * 根据订单id创建orderDto
     * @param orderId
     * @return
     */
    private OrderDto createOrderDtoByOrderId(Long orderId) {
        OrderDto result = new OrderDto();

        Order order = orderMapper.getById(orderId);
        BeanUtils.copyProperties(order, result);

        return result;
    }
}
