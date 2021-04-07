package com.library.manage.model.vo;

import lombok.Data;

/**
 * @author whz
 * @Email: whz@dofun.cc
 * @ClassName: QueryMessageVO
 * @Date: 2021/3/31 11:47
 * @Description:
 */
@Data
public class QueryMessageVO extends PageVO {
    private String status;
    private String type;
    private String message;
}
