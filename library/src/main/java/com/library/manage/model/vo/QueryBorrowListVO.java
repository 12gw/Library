package com.library.manage.model.vo;

import lombok.Data;


@Data
public class QueryBorrowListVO extends PageVO {
    private String username;
    private String type;
    private String status;
    private String bookname;
}
