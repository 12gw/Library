package com.library.manage.model.vo;


import lombok.Data;

@Data
public class UpdateMessageVO {
    private String message;
    private Integer id;
    private String status;
    private String type;
}
