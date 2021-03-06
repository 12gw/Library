package com.library.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("borrow")
public class Borrow {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String type;
    private String bookname;
    private String status;
    private String mail;
    private Integer money;
    private Integer bookid;
    private String borrowTime;
    private String returnTime;

}
