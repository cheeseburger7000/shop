package com.shaohsiung.shop.service;

import com.shaohsiung.shop.dto.CategoryDto;
import com.shaohsiung.shop.mapper.CategoryMapper;
import com.shaohsiung.shop.model.Category;
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
 * 商品类目模块
 */
@Slf4j
@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加商品分类
     * @param category
     * @return
     */
    public CategoryDto addCategory(Category category) {
        category.setCreateTime(new Date());

        categoryMapper.save(category);
        log.info("【商品类目模块】添加分类：{}", category);

        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(category, categoryDto);
        return categoryDto;
    }

    /**
     * 获取分类列表
     * @param pageNum 从0开始
     * @param pageSize
     * @return
     */
    public List<CategoryDto> categoryList(int pageNum, int pageSize) {
        List<CategoryDto>  result = new ArrayList();

        List<Category> categoryList = categoryMapper.findAll(new RowBounds(pageNum * pageSize, pageSize));
        log.info("【商品类目模块】分类列表：{}", categoryList);

        categoryList.forEach(category -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category, categoryDto);
            result.add(categoryDto);
        });
        return result;
    }
}
