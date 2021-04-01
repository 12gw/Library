package com.library.manage.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.manage.VO.GetBookListVO;
import com.library.manage.VO.PageVO;
import com.library.manage.entity.Book;
import com.library.manage.mapper.BookMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Evan
 * @date 2019/4
 */
@Service
public class BookService {
    @Autowired
    private BookMapper bookDAO;

    @Autowired
    private UserService userService;

    /**
     * 图书列表
     *
     * @return
     */
    public List<Book> list() {
        return bookDAO.selectList((Wrappers.<Book>lambdaQuery().orderByDesc(Book::getId)));
    }

    /**
     * 图书管理 - 图书列表
     *
     * @return
     */
    public Page<Book> getBookList(GetBookListVO vo) {
        Subject subject = SecurityUtils.getSubject();
        String name = subject.getPrincipal().toString();
        Page<Book> page = new Page<>(vo.getCurrent(), vo.getSize());
        Integer level = userService.findByUsername(name).getLevel();
        if (level < 10) {
            page.setRecords(bookDAO.getBookList(page, vo.getKeywords(), vo.getCid(),null));
            return page;
        } else if (level <= 20 && level >= 10) {
            page.setRecords(bookDAO.getBookList(page,vo.getKeywords(), vo.getCid(), level));
            return page;
        } else {
            return null;
        }
    }

    public void addOrUpdate(Book book) {
        bookDAO.insert(book);
    }

    public void deleteById(int id) {
        bookDAO.deleteById(id);
    }

    public List<Book> listByCategory(int cid) {
        return bookDAO.selectList(Wrappers.<Book>lambdaQuery().eq(Book::getCid, cid));
    }

    public List<Book> Search(String keywords) {
        return bookDAO.searchList(keywords);
    }
}
