package com.library.manage.model.vo;

import lombok.Data;


@Data
public class UpdateBorrowVO {
    private String username;
    private String status;
    private Integer money;
    private Integer bookid;
    private Integer id;
}
