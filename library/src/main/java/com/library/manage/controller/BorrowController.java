package com.library.manage.controller;

import com.library.manage.config.result.Result;
import com.library.manage.config.result.ResultFactory;
import com.library.manage.model.vo.QueryBorrowListVO;
import com.library.manage.model.vo.SaveBorrowVO;
import com.library.manage.model.vo.UpdateBorrowVO;
import com.library.manage.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class BorrowController {
    @Autowired
    private BorrowService borrowService;

    @PostMapping("/api/admin/saveBorrow")
    public Result saveBorrow(@Valid @RequestBody SaveBorrowVO vo) {
        int i = borrowService.saveBorrow(vo.getBookid(), vo.getBookname(), vo.getType(), vo.getMailAccount());
        if (i == 0) {
            return ResultFactory.buildFailResult("保存失败！");
        } else if (i == 2) {
            return ResultFactory.buildFailResult("由于您经常超期还书，现在不能借阅书籍！");
        } else if (i == 3) {
            return ResultFactory.buildFailResult("暂无库存，只能预约");
        } else if (i == 4) {
            return ResultFactory.buildFailResult("预约成功，请及时关注本书");
        }
        return ResultFactory.buildSuccessResult(i);
    }

    @GetMapping("/api/getBorrowList")
    public Result getBorrowList(QueryBorrowListVO vo) {
        return ResultFactory.buildSuccessResult(borrowService.getBorrowList(vo));
    }

    @PutMapping("/api/admin/borrow/updateBorrow")
    public Result updateBorrow(@RequestBody UpdateBorrowVO vo) {
        int i = borrowService.updateBorrow(vo.getUsername(), vo.getStatus(), vo.getMoney(), vo.getBookid(), vo.getId());
        if (i == 0) {
            return ResultFactory.buildFailResult("更改失败");
        }else if (i == 2){
            return ResultFactory.buildFailResult("请先还书再缴纳罚款");
        }
        return ResultFactory.buildSuccessResult(i);
    }

}
