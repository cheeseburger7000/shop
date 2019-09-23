package com.shaohsiung.shop.mapper;

import com.shaohsiung.shop.model.Category;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("insert into t_category(name, create_time) values(#{name}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "categoryId")
    int save(Category category);

    @Select("select * from t_category order by create_time desc")
    List<Category> findAll(RowBounds rowBounds);

    @Select("select name from t_category where category_id = #{CategoryId}")
    String getCategoryNameById(@Param("CategoryId") Long CategoryId);
}
