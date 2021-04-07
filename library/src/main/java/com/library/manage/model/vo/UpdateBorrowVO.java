package com.library.manage.model.vo;

import lombok.Data;

/**
 * @author whz
 * @Email: whz@dofun.cc
 * @ClassName: UpdateBorrowVO
 * @Date: 2021/3/26 11:08
 * @Description:
 */
@Data
public class UpdateBorrowVO {
    private String username;
    private String status;
    private Integer money;
    private Integer bookid;
    private Integer id;
}
