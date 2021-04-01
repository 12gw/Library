package com.library.manage.VO;

import lombok.Data;

/**
 * @author whz
 * @Email: whz@dofun.cc
 * @ClassName: QueryBorrowListVO
 * @Date: 2021/3/29 17:19
 * @Description:
 */
@Data
public class QueryBorrowListVO extends PageVO {
    private String username;
    private String type;
    private String status;
    private String bookname;
}
