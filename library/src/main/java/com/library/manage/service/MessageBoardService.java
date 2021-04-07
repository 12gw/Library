package com.library.manage.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.manage.model.vo.QueryMessageVO;
import com.library.manage.entity.MessageBoard;
import com.library.manage.mapper.MessageBoardMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageBoardService {
    @Autowired
    private MessageBoardMapper messageBoardmapper;

    /**
     * 保存留言
     *
     * @param message
     * @param type
     * @return
     */
    public int saveMessage(String message, String type) {
        MessageBoard messageBoard = new MessageBoard();
        messageBoard.setMessage(message);
        messageBoard.setType(type);
        Subject subject = SecurityUtils.getSubject();
        String username = subject.getPrincipal().toString();
        messageBoard.setUsername(username);
        return messageBoardmapper.insert(messageBoard);
    }

    /**
     * 分页查询留言板的信息
     *
     * @return
     */
    public Page<MessageBoard> messageBoardList(QueryMessageVO vo) {
        Page<MessageBoard> page = new Page<>(vo.getCurrent(), vo.getSize());
        page.setRecords(messageBoardmapper.selectPage(page, Wrappers.<MessageBoard>lambdaQuery()
                .eq(StrUtil.isNotBlank(vo.getStatus()), MessageBoard::getStatus, vo.getStatus())
                .eq(StrUtil.isNotBlank(vo.getType()), MessageBoard::getType, vo.getType())
                .like(StrUtil.isNotBlank(vo.getMessage()), MessageBoard::getMessage, vo.getMessage())
                .orderByDesc(MessageBoard::getCreateTime))
                .getRecords());
        return page;
    }


    /**
     * 更新状态或者内容
     *
     * @param message
     * @param status
     * @param id
     * @return
     */
    public int updateMessage(String message, String status, Integer id, String type) {
        MessageBoard messageBoard = new MessageBoard();
        if (!StringUtils.isEmpty(status)) {
            messageBoard.setStatus(status);
        }
        if (!StringUtils.isEmpty(type)) {
            messageBoard.setType(type);
        }
        if (!StringUtils.isEmpty(message)) {
            messageBoard.setMessage(message);
        }
        return messageBoardmapper.update(messageBoard, Wrappers.<MessageBoard>lambdaUpdate().eq(MessageBoard::getId, id));
    }
}
