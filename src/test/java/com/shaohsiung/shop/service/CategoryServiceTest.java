package com.shaohsiung.shop.service;

import com.shaohsiung.shop.dto.CategoryDto;
import com.shaohsiung.shop.model.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Test
    public void addCategory() {
        Category coffee = Category.builder().name("咖啡")
                .build();
        CategoryDto categoryDto = categoryService.addCategory(coffee);
    }

    @Test
    public void categoryList() {
        List<CategoryDto> categoryDtos = categoryService.categoryList(0, 2);
    }
}