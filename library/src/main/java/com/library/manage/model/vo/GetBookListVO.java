package com.library.manage.model.vo;

import lombok.Data;


@Data
public class GetBookListVO extends PageVO {
    private String keywords;
    private Integer cid;
}
