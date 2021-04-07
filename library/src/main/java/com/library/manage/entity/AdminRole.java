package com.library.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("admin_role")
@ToString
public class AdminRole {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * Role name.
     */
    private String name;

    /**
     * Role name in Chinese.
     */
    private String nameZh;

    /**
     * Role status.
     */
    private boolean enabled;


    /**
     * Transient property for storing permissions owned by current role.
     */
    @TableField(exist = false)
    private List<AdminPermission> perms;

    /**
     * Transient property for storing menus owned by current role.
     */
    @TableField(exist = false)
    private List<AdminMenu> menus;

    @TableField(exist = false)
    private List<Integer> menuIds;
}
