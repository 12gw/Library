package com.library.manage.model.vo;

import lombok.Data;

import javax.validation.constraints.Email;


@Data
public class SaveBorrowVO {
    private Integer bookid;
    private String bookname;
    private String type;
    private String status;

    @Email(message = "邮件格式错误")
    private String mailAccount;
}
