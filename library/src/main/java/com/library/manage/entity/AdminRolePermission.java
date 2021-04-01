package com.library.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * Relations between roles and permissions.
 *
 * @author Evan
 * @date 2019/11
 */
@Data
@TableName("admin_role_permission")
@ToString
public class AdminRolePermission {
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * Role id.
     */
    private int rid;

    /**
     * Permission id.
     */
    private int pid;
}
