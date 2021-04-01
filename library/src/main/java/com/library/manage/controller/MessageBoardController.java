package com.library.manage.controller;

import com.library.manage.VO.QueryMessageVO;
import com.library.manage.result.Result;
import com.library.manage.result.ResultFactory;
import com.library.manage.service.MessageBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageBoardController {
    @Autowired
    private MessageBoardService messageBoardService;

    @PostMapping("/api/admin/saveMessage")
    public Result saveMessage(@RequestParam(value = "message", required = false) String message,
                              @RequestParam(value = "type", required = false) String type) {
        int i = messageBoardService.saveMessage(message, type);
        if (i == 0) {
            return ResultFactory.buildFailResult("保存失败");
        }
        return ResultFactory.buildSuccessResult(i);
    }

    @GetMapping("/api/messageBoardList")
    public Result messageBoardList(QueryMessageVO vo) {
        return ResultFactory.buildSuccessResult(messageBoardService.messageBoardList(vo));
    }

    @PutMapping("/api/admin/updateMessage")
    public Result updateMessage(@RequestParam(value = "message", required = false) String message,
                                @RequestParam(value = "id", required = false) Integer id,
                                @RequestParam(value = "status", required = false) String status,
                                @RequestParam(value = "type", required = false) String type) {
        int i = messageBoardService.updateMessage(message, status, id, type);
        if (i == 0) {
            return ResultFactory.buildFailResult("更改失败");
        }
        return ResultFactory.buildSuccessResult(i);
    }
}