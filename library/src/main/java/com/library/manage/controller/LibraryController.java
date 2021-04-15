package com.library.manage.controller;

import com.library.manage.config.result.Result;
import com.library.manage.config.result.ResultFactory;
import com.library.manage.entity.Book;
import com.library.manage.model.vo.GetBookListVO;
import com.library.manage.service.BookService;
import com.library.manage.service.CategoryService;
import com.library.manage.service.MessageBoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Random;

@Api(tags = "图书管理")
@RestController
public class LibraryController {
    @Autowired
    private BookService bookService;

    @Autowired
    private MessageBoardService messageBoardService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/books")
    @ApiOperation(value = "查询图书列表 - 主页")
    public Result listBooks() {
        return ResultFactory.buildSuccessResult(bookService.list());
    }

    @GetMapping("/api/bookLists")
    @ApiOperation(value = "查询图书列表 - 管理")
    public Result getBookList(GetBookListVO vo) {
        return ResultFactory.buildSuccessResult(bookService.getBookList(vo));
    }

    @PostMapping("/api/admin/content/books")
    @ApiOperation(value = "修改图书信息")
    public Result addOrUpdateBooks(@RequestBody @Valid Book book) {
        bookService.addOrUpdate(book);
        return ResultFactory.buildSuccessResult("修改成功");
    }

    @PostMapping("/api/admin/content/books/delete")
    @ApiOperation(value = "删除图书")
    public Result deleteBook(@RequestBody @Valid Book book) {
        bookService.deleteById(book.getId());
        return ResultFactory.buildSuccessResult("删除成功");
    }

    @GetMapping("/api/search")
    @ApiOperation(value = "搜索图书")
    public Result searchResult(@RequestParam("keywords") String keywords) {
        return ResultFactory.buildSuccessResult(bookService.Search(keywords));
    }

    @GetMapping("/api/categories/books")
    @ApiOperation(value = "根据分类获取书籍")
    public Result listByCategory(int cid) throws Exception {
        if (0 != cid) {
            return ResultFactory.buildSuccessResult(bookService.listByCategory(cid));
        } else {
            return listBooks();
        }
    }

    @PostMapping("/api/admin/content/books/covers")
    @ApiOperation(value = "上传图书封面")
    public String coversUpload(MultipartFile file) throws IOException {
//        String folder = "C:/workspace/img";
//        File imageFolder = new File(folder);
        // 获取static目录 获取static就可以完成上传功能了
        String staticPath = ClassUtils.getDefaultClassLoader().getResource("static/img").getPath();
        File f = new File(staticPath, getRandomString(6) + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 4));
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
    @ApiOperation(value = "图书类别")
    public Result categories() {
        return ResultFactory.buildSuccessResult(categoryService.list());
    }

    /**
     * 获取随机数
     *
     * @param length
     * @return
     */
    private static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
