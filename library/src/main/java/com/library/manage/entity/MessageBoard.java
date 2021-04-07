package com.library.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("message_board")
public class MessageBoard {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String message;
    private String username;
    private String type;
    private String createTime;
    private String status;
}
