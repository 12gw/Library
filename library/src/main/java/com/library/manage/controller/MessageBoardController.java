package com.library.manage.controller;

import com.library.manage.config.result.Result;
import com.library.manage.config.result.ResultFactory;
import com.library.manage.model.vo.QueryMessageVO;
import com.library.manage.model.vo.UpdateMessageVO;
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

    @PutMapping("/api/admin/message/updateMessage")
    public Result updateMessage(UpdateMessageVO vo) {
        int i = messageBoardService.updateMessage(vo.getMessage(), vo.getStatus(), vo.getId(), vo.getType());
        if (i == 0) {
            return ResultFactory.buildFailResult("更改失败");
        }
        return ResultFactory.buildSuccessResult(i);
    }
}
