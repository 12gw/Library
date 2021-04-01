package com.library.manage.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * Relations between roles and menus.
 *
 * @author Evan
 * @date 2019/11
 */
@Data
@TableName("admin_role_menu")
@ToString
public class AdminRoleMenu {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * Role id.
     */
    private int rid;

    /**
     * Menu id.
     */
    private int mid;
}
