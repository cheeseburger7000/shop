package com.shaohsiung.shop.handler;

import com.shaohsiung.shop.model.enums.UserStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserStatusTypeHandler extends BaseTypeHandler<UserStatus> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, UserStatus userStatus, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, userStatus.getCode());
    }

    @Override
    public UserStatus getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return parseUserStatus(resultSet.getInt(s));
    }

    @Override
    public UserStatus getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return parseUserStatus(resultSet.getInt(i));
    }

    @Override
    public UserStatus getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return parseUserStatus(callableStatement.getInt(i));
    }

    private UserStatus parseUserStatus(Integer value) {
        if (value == 0) {
            return UserStatus.INACTIVATED;
        } else if (value == 1) {
            return UserStatus.NORMAL;
        } else if (value == 2) {
            return UserStatus.FREEZE;
        }
        throw new RuntimeException("用户状态转换错误");
    }
}
