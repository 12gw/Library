package com.library.manage.controller;

import com.library.manage.VO.QueryBorrowListVO;
import com.library.manage.VO.SaveBorrowVO;
import com.library.manage.VO.UpdateBorrowVO;
import com.library.manage.result.Result;
import com.library.manage.result.ResultFactory;
import com.library.manage.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BorrowController {
    @Autowired
    private BorrowService borrowService;

    @PostMapping("/api/admin/saveBorrow")
    public Result saveBorrow(@RequestBody SaveBorrowVO vo) {
        int i = borrowService.saveBorrow(vo.getBookid(), vo.getBookname(), vo.getType(), vo.getMailAccount());
        if (i == 0) {
            return ResultFactory.buildFailResult("保存失败！");
        } else if (i == 2) {
            return ResultFactory.buildFailResult("由于您经常超期还书，现在不能借阅书籍！");
        }
        return ResultFactory.buildSuccessResult(i);
    }

    @GetMapping("/api/getBorrowList")
    public Result getBorrowList(QueryBorrowListVO vo) {
        return ResultFactory.buildSuccessResult(borrowService.getBorrowList(vo));
    }

    @PutMapping("/api/admin/updateBorrow")
    public Result updateBorrow(@RequestBody UpdateBorrowVO vo) {
        int i = borrowService.updateBorrow(vo.getUsername(), vo.getStatus(), vo.getMoney(), vo.getBookid(), vo.getId());
        if (i == 0) {
            return ResultFactory.buildFailResult("更改失败");
        } else if (i == 2) {
            return ResultFactory.buildFailResult("不是自己的借阅记录，不能更改");
        }
        return ResultFactory.buildSuccessResult(i);
    }

}
