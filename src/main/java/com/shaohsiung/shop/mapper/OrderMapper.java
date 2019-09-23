package com.shaohsiung.shop.mapper;

import com.shaohsiung.shop.model.Order;
import com.shaohsiung.shop.model.OrderItem;
import com.shaohsiung.shop.model.enums.OrderStatus;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("insert into t_order(order_no, total, status, user_id, create_time) values(#{orderNo}, #{total}, #{status}, #{userId}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    int save(Order order);

    @Insert("insert into t_order_item(order_id, goods_id, count, amount) values(#{orderId}, #{goodsId}, #{count}, #{amount})")
    @Options(useGeneratedKeys = true, keyProperty = "orderItemId")
    int saveOrderItem(OrderItem orderItem);

    @Select("select status from t_order where order_id = #{orderId} and user_id = #{userId}")
    OrderStatus getOrderStatusByUserIdAndOrderId(@Param("userId") Long userId, @Param("orderId") Long orderId);

    @Update("update t_order set status = #{status} where order_id = #{orderId}")
    int updateOrderStatusByOrderId(@Param("status") OrderStatus status, @Param("orderId") Long orderId);

    @Select("select * from t_order where order_id = #{orderId}")
    Order getById(Long orderId);

    @Select("select * from t_order_item where order_id = #{orderId}")
    @Results
    List<OrderItem> getOrderItemListByOrderId(Long orderId);

    @Select("select * from t_order where order_no = #{orderNo}")
    Order getByOrderNo(String orderNo);

    @Select("select * from t_order where user_id = #{userId} order by status")
    @Results
    List<Order> getOrderListByUserId(@Param("userId") Long userId, RowBounds rowBounds);

    @Select("select * from t_order order by status")
    @Results
    List<Order> getOrderList(RowBounds rowBounds);
}
