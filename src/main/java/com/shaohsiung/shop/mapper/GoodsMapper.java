package com.shaohsiung.shop.mapper;

import com.shaohsiung.shop.model.Goods;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface GoodsMapper {
    @Insert("insert into t_goods (name, price, stock, status, create_time, category_id, image)"
            + "values (#{name}, #{price}, #{stock}, #{status}, #{createTime}, #{categoryId}, #{image})")
    @Options(useGeneratedKeys = true, keyProperty = "goodsId")
    int save(Goods goods); // int返回值为1说明操作成功

    @Select("select * from t_goods order by status, create_time desc")
    List<Goods> goodsList(RowBounds rowBounds);

    @Select("select * from t_goods where category_id = #{categoryId} order by status, create_time desc")
    List<Goods> goodsListByCategoryId(@Param("categoryId") Long categoryId, RowBounds rowBounds);

    @Select("select * from t_goods where goods_id = #{goodsId}")
    Goods getById(@Param("goodsId") Long goodsId);

    @Select("select * from t_goods where name like #{keyword} order by status, create_time desc")
    List<Goods> search(String keyword, RowBounds rowBounds);

    @Update("update t_goods set stock = #{stock} where goods_id = #{goodsId}")
    int updateGoodsStock(@Param("goodsId") Long goodsId, @Param("stock") Integer stock);

    @Update("update t_goods set category_id = #{categoryId} where goods_id = #{goodsId}")
    int updateGoodsCategory(@Param("goodsId") Long goodsId, @Param("categoryId") Long categoryId);

    @Update("update t_goods set status = #{status} where goods_id = #{goodsId}")
    int updateGoodsStatus(@Param("goodsId") Long goodsId, @Param("status") Integer status);

    @Select("select status from t_goods where goods_id = #{goodsId}")
    Integer getGoodsStatusById(Long goodsId);
}
