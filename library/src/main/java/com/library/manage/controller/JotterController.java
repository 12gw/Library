package com.library.manage.controller;

import com.library.manage.config.result.Result;
import com.library.manage.config.result.ResultFactory;
import com.library.manage.entity.JotterArticle;
import com.library.manage.model.vo.PageVO;
import com.library.manage.service.JotterArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class JotterController {
    @Autowired
    private JotterArticleService jotterArticleService;

    @PostMapping("api/admin/content/article")
    public Result saveArticle(@RequestBody @Valid JotterArticle article) {
        jotterArticleService.addOrUpdate(article);
        return ResultFactory.buildSuccessResult("保存成功");
    }

    @GetMapping("/api/article")
    public Result listArticles(PageVO vo) {
        return ResultFactory.buildSuccessResult(jotterArticleService.list(vo.getCurrent(), vo.getSize()));
    }

    @GetMapping("/api/admin/article")
    public Result getOneArticle(int id) {
        return ResultFactory.buildSuccessResult(jotterArticleService.findById(id));
    }

    @DeleteMapping("/api/admin/content/article")
    public Result deleteArticle(int id) {
        jotterArticleService.delete(id);
        return ResultFactory.buildSuccessResult("删除成功");
    }
}
