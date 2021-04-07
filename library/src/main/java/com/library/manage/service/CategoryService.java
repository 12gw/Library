package com.library.manage.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.library.manage.entity.Category;
import com.library.manage.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    public List<Category> list() {
        return categoryMapper.selectList(Wrappers.<Category>lambdaQuery().orderByDesc(Category::getId));
    }

    public Category get(int id) {
        return categoryMapper.selectOne(Wrappers.<Category>lambdaQuery().eq(Category::getId, id));
    }
}
