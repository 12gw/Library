package com.library.manage.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.manage.entity.JotterArticle;
import com.library.manage.mapper.JotterArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JotterArticleService {
    @Autowired
    JotterArticleMapper jotterArticleMapper;

    public Page<JotterArticle> list(long current, long size) {
        Page<JotterArticle> articlesInDb = new Page<>(current, size);
        articlesInDb.setRecords(jotterArticleMapper.selectPage(articlesInDb, Wrappers.<JotterArticle>lambdaQuery().orderByDesc(JotterArticle::getId)).getRecords());
        return articlesInDb;
    }

    public JotterArticle findById(int id) {
        JotterArticle article = jotterArticleMapper.selectOne(Wrappers.<JotterArticle>lambdaQuery().eq(JotterArticle::getId, id));
        return article;
    }

    public void addOrUpdate(JotterArticle article) {
        jotterArticleMapper.insert(article);
    }

    public void delete(int id) {
        jotterArticleMapper.deleteById(id);
    }
}
