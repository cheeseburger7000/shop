package com.shaohsiung.shop.handler;

import com.shaohsiung.shop.model.enums.GoodsStatus;
import com.shaohsiung.shop.model.enums.OrderStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderStatusTypeHandler extends BaseTypeHandler<OrderStatus> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, OrderStatus orderStatus, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, orderStatus.getCode());
    }

    @Override
    public OrderStatus getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return parseOrderStatus(resultSet.getInt(s));
    }

    @Override
    public OrderStatus getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return parseOrderStatus(resultSet.getInt(i));
    }

    @Override
    public OrderStatus getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return parseOrderStatus(callableStatement.getInt(i));
    }

    private OrderStatus parseOrderStatus(Integer value) {
        if (value == 0) {
            return OrderStatus.NOT_SHIPPED;
        } else if (value == 1) {
            return OrderStatus.CANCEL;
        } else if (value == 2) {
            return OrderStatus.SHIPPED;
        } else if (value == 3) {
            return OrderStatus.COMPLETED;
        } else if (value == 4) {
            return OrderStatus.CLOSED;
        }
        throw new RuntimeException("订单状态转换错误");
    }
}
