package com.library.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * Category entity.
 *
 * @author Evan
 * @date 2019/4
 */
@Data
@TableName("category")
@ToString
public class Category {
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * Category name in Chinese.
     */
    private String name;
}
