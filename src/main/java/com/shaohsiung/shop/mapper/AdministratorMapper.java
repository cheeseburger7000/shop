package com.shaohsiung.shop.mapper;

import com.shaohsiung.shop.model.Administrator;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdministratorMapper {
    @Select("select * from t_admin where admin_name = #{adminName} and password = #{password}")
    Administrator findAdministratorByAdminNameAndPassword(@Param("adminName") String adminName, @Param("password") String encodedPassword);
}
