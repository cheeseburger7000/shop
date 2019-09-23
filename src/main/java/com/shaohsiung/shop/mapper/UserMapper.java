package com.shaohsiung.shop.mapper;

import com.shaohsiung.shop.model.User;
import com.shaohsiung.shop.model.enums.UserStatus;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into t_user(user_name, password, mobile, status) values(#{userName}, #{password}, #{mobile}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int save(User user);

    @Select("select * from t_user where user_name = #{userName} and password = #{password}")
    @Results
    User findUserByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

    @Select("select * from t_user order by status")
    List<User> findAll(RowBounds rowBounds);

    @Update("update t_user set status = #{status} where user_id = #{userId}")
    int updateUserStatus(@Param("userId") Long userId, @Param("status") int status);

    @Select("select * from t_user where user_id = #{userId}")
    User getById(Long userId);
}
