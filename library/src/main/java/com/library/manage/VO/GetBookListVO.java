package com.library.manage.VO;

import lombok.Data;

/**
 * @author whz
 * @Email: whz@dofun.cc
 * @ClassName: GetBookListVO
 * @Date: 2021/3/24 16:04
 * @Description:
 */
@Data
public class GetBookListVO extends PageVO{
    private String keywords;
    private Integer cid;
}
