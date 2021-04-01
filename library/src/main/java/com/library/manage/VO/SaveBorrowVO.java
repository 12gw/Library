package com.library.manage.VO;

import lombok.Data;

import javax.validation.constraints.Email;

/**
 * @author whz
 * @Email: whz@dofun.cc
 * @ClassName: SaveBorrowVO
 * @Date: 2021/3/26 11:13
 * @Description:
 */
@Data
public class SaveBorrowVO {
    private Integer bookid;
    private String bookname;
    private String type;
    private String status;

    @Email(message = "wrong email format")
    private String mailAccount;
}
