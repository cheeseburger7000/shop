package com.shaohsiung.shop.api;

import com.shaohsiung.shop.dto.GoodsDto;
import com.shaohsiung.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/{pageNum}/{pageSize}")
    public String goodsList(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize, Model model) {
        List<GoodsDto> goodsList = goodsService.goodsList(pageNum, pageSize);
        model.addAttribute("goodsList", goodsList);
        return "list";
    }
}