package com.library.manage.controller;

import com.library.manage.VO.GetBookListVO;
import com.library.manage.VO.PageVO;
import com.library.manage.entity.Book;
import com.library.manage.result.Result;
import com.library.manage.result.ResultFactory;
import com.library.manage.service.BookService;
import com.library.manage.service.CategoryService;
import com.library.manage.service.MessageBoardService;
import com.library.manage.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@RestController
public class LibraryController {
    @Autowired
    BookService bookService;

    @Autowired
    MessageBoardService messageBoardService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/api/books")
    public Result listBooks() {
        return ResultFactory.buildSuccessResult(bookService.list());
    }

    @GetMapping("/api/bookLists")
    public Result getBookList(GetBookListVO vo) {
        return ResultFactory.buildSuccessResult(bookService.getBookList(vo));
    }

    @PostMapping("/api/admin/content/books")
    public Result addOrUpdateBooks(@RequestBody @Valid Book book) {
        bookService.addOrUpdate(book);
        return ResultFactory.buildSuccessResult("修改成功");
    }

    @PostMapping("/api/admin/content/books/delete")
    public Result deleteBook(@RequestBody @Valid Book book) {
        bookService.deleteById(book.getId());
        return ResultFactory.buildSuccessResult("删除成功");
    }

    @GetMapping("/api/search")
    public Result searchResult(@RequestParam("keywords") String keywords) {
        return ResultFactory.buildSuccessResult(bookService.Search(keywords));
    }

    @GetMapping("/api/categories/books")
    public Result listByCategory(int cid) throws Exception {
        if (0 != cid) {
            return ResultFactory.buildSuccessResult(bookService.listByCategory(cid));
        } else {
            return listBooks();
        }
    }

    @PostMapping("/api/admin/content/books/covers")
    public String coversUpload(MultipartFile file) {
        String folder = "C:/workspace/img";
        File imageFolder = new File(folder);
        File f = new File(imageFolder, StringUtils.getRandomString(6) + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            String imgURL = "http://localhost:8443/api/file/" + f.getName();
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping("/api/admin/content/categories")
    public Result categories() {
        return ResultFactory.buildSuccessResult(categoryService.list());
    }
}
