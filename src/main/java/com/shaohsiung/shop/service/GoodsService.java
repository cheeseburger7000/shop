package com.shaohsiung.shop.service;

import com.shaohsiung.shop.dto.GoodsDto;
import com.shaohsiung.shop.error.ServiceException;
import com.shaohsiung.shop.mapper.CategoryMapper;
import com.shaohsiung.shop.mapper.GoodsMapper;
import com.shaohsiung.shop.model.Category;
import com.shaohsiung.shop.model.Goods;
import com.shaohsiung.shop.model.enums.GoodsStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品模块
 */
@Slf4j
@Service
@Transactional
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加商品
     * @param goods
     * @return
     */
    GoodsDto addGoods(Goods goods) {
        goods.setCreateTime(new Date());
        goods.setStatus(GoodsStatus.ON_THE_SHELF);

        goodsMapper.save(goods);
        log.info("【商品模块】添加商品：{}", goods);

        GoodsDto goodsDto = new GoodsDto();
        BeanUtils.copyProperties(goods, goodsDto);

        // 设置商品类目名称
        String categoryName = categoryMapper.getCategoryNameById(goods.getCategoryId());
        goodsDto.setCategoryName(categoryName);
        return goodsDto;
    }

    /**
     * 获取商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<GoodsDto> goodsList(int pageNum, int pageSize) {
        List<GoodsDto> result = new ArrayList<>();

        List<Goods> goodsList = goodsMapper.goodsList(new RowBounds(pageNum, pageSize));
        log.info("【商品模块】获取商品列表：{}", goodsList);

        // TODO 重构抽取重复代码
        goodsList.forEach(goods -> {
            GoodsDto goodsDto = new GoodsDto();
            BeanUtils.copyProperties(goods, goodsDto);

            // 设置商品类目名称
            String categoryName = categoryMapper.getCategoryNameById(goods.getCategoryId());
            goodsDto.setCategoryName(categoryName);

            result.add(goodsDto);
        });
        return result;
    }

    /**
     * 根据分类id获取商品列表
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<GoodsDto> goodsListByCategoryId(Long categoryId, int pageNum, int pageSize) {
        List<GoodsDto> result = new ArrayList<>();

        List<Goods> goodsList = goodsMapper.goodsListByCategoryId(categoryId, new RowBounds(pageNum, pageSize));
        log.info("【商品模块】根据categoryId：{} 获取商品列表：{}", categoryId, goodsList);

        goodsList.forEach(goods -> {
            GoodsDto goodsDto = new GoodsDto();
            BeanUtils.copyProperties(goods, goodsDto);

            // 设置商品类目名称
            String categoryName = categoryMapper.getCategoryNameById(goods.getCategoryId());
            goodsDto.setCategoryName(categoryName);

            result.add(goodsDto);
        });
        return result;
    }

    /**
     * 根据商品id获取商品
     * @param goodsId
     * @return
     */
    GoodsDto getById(Long goodsId) {
        Goods goods = goodsMapper.getById(goodsId);
        log.info("【商品模块】根据goodsId：{} 获取商品：{}", goodsId, goods);

        GoodsDto result = new GoodsDto();
        BeanUtils.copyProperties(goods, result);

        // 设置商品类目名称
        String categoryName = categoryMapper.getCategoryNameById(goods.getCategoryId());
        result.setCategoryName(categoryName);

        return result;
    }

    /**
     * 模糊查询。按在架状态升序，创建时间降序
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<GoodsDto> search(String keyword, int pageNum, int pageSize) {
        List<GoodsDto> result = new ArrayList<>();
        List<Goods> goodsList = goodsMapper.search("%" + keyword + "%", new RowBounds(pageNum, pageSize));
        log.info("【商品模块】关键词：{} 商品列表：", keyword, goodsList);

        goodsList.forEach(goods -> {
            GoodsDto goodsDto = new GoodsDto();
            BeanUtils.copyProperties(goods, goodsDto);

            // 设置商品类目名称
            String categoryName = categoryMapper.getCategoryNameById(goods.getCategoryId());
            goodsDto.setCategoryName(categoryName);

            result.add(goodsDto);
        });
        return result;
    }

    /**
     * 修改商品库存
     * @param goodsId
     * @param stock
     * @return
     */
    public GoodsDto updateGoodsStock(Long goodsId, Integer stock) {
        int updated = goodsMapper.updateGoodsStock(goodsId, stock);
        if (updated == 1) {
            log.info("【商品模块】更新商品库存 goodsId：{}, stock：{}", goodsId, stock);
            Goods goods = goodsMapper.getById(goodsId);
            GoodsDto result = new GoodsDto();
            BeanUtils.copyProperties(goods, result);

            String categoryName = categoryMapper.getCategoryNameById(goods.getCategoryId());
            result.setCategoryName(categoryName);
            return  result;
        }

        log.error("【商品模块】商品库存更新失败 goodsId: {}, stock: {}", goodsId, stock);
        throw new ServiceException("商品库存更新失败");
    }

    /**
     * 更新商品的商品类目
     * @param goodsId
     * @param categoryId
     * @return
     */
    public GoodsDto updateGoodsCategory(Long goodsId, Long categoryId) {
        int updated = goodsMapper.updateGoodsCategory(goodsId, categoryId);
        if (updated == 1) {
            log.info("【商品模块】更新商品类目 goodsId：{} categoryId：{}", goodsId, categoryId);
            Goods goods = goodsMapper.getById(goodsId);
            GoodsDto result = new GoodsDto();
            BeanUtils.copyProperties(goods, result);

            String categoryName = categoryMapper.getCategoryNameById(goods.getCategoryId());
            result.setCategoryName(categoryName);
            return  result;
        }

        log.error("【商品模块】商品类目更新失败 goodsId: {}, categoryId: {}", goodsId, categoryId);
        throw new ServiceException("商品类目更新失败");
    }

    /**
     * 下架商品
     * 后台
     * 功能描述：商品状态若是上架中，那么就转变为已下架。反之亦然。
     * @param goodsId
     * @return
     */
    public GoodsDto updateGoodsStatus(Long goodsId) {
        // 获取商品当前状态
        Integer status = goodsMapper.getGoodsStatusById(goodsId);
        if (status == GoodsStatus.ON_THE_SHELF.getCode()) {
            // 上架中
            int updated = goodsMapper.updateGoodsStatus(goodsId, GoodsStatus.HAS_BEEN_REMOVED.getCode());
            if (updated == 1) {
                log.info("【商品模块】更新商品状态 goodsId：{} status：{}", goodsId, GoodsStatus.HAS_BEEN_REMOVED.getCode());
                Goods newGoods = goodsMapper.getById(goodsId);
                GoodsDto result = new GoodsDto();
                BeanUtils.copyProperties(newGoods, result);

                String categoryName = categoryMapper.getCategoryNameById(newGoods.getCategoryId());
                result.setCategoryName(categoryName);
                return  result;
            }
        } else if (status == GoodsStatus.HAS_BEEN_REMOVED.getCode()) {
            // 已下架
            int updated = goodsMapper.updateGoodsStatus(goodsId, GoodsStatus.ON_THE_SHELF.getCode());
            if (updated == 1) {
                log.info("【商品模块】更新商品状态 goodsId：{} status：{}", goodsId, GoodsStatus.ON_THE_SHELF.getCode());
                Goods newGoods = goodsMapper.getById(goodsId);
                GoodsDto result = new GoodsDto();
                BeanUtils.copyProperties(newGoods, result);

                String categoryName = categoryMapper.getCategoryNameById(newGoods.getCategoryId());
                result.setCategoryName(categoryName);
                return  result;
            }
        }

        log.error("【商品模块】商品状态更新失败 goodsId: {}", goodsId);
        throw new ServiceException("商品状态更新失败");
    }
}
