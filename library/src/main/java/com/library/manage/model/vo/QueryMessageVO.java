package com.library.manage.model.vo;

import lombok.Data;


@Data
public class QueryMessageVO extends PageVO {
    private String status;
    private String type;
    private String message;
}
