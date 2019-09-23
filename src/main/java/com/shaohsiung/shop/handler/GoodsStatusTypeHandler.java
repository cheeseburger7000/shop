package com.shaohsiung.shop.handler;

import com.shaohsiung.shop.model.enums.GoodsStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GoodsStatusTypeHandler extends BaseTypeHandler<GoodsStatus> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, GoodsStatus goodsStatus, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, goodsStatus.getCode());
    }

    @Override
    public GoodsStatus getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return parseGoodsStatus(resultSet.getInt(s));
    }

    @Override
    public GoodsStatus getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return parseGoodsStatus(resultSet.getInt(i));
    }

    @Override
    public GoodsStatus getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return parseGoodsStatus(callableStatement.getInt(i));
    }

    private GoodsStatus parseGoodsStatus(Integer value) {
        if (value == 0) {
            return GoodsStatus.ON_THE_SHELF;
        } else if (value == 1) {
            return GoodsStatus.HAS_BEEN_REMOVED;
        }
        throw new RuntimeException("商品状态转换错误"); // TODO 定义TypeHandleException
    }
}
