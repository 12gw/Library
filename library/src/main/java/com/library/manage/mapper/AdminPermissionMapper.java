package com.library.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.library.manage.entity.AdminPermission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminPermissionMapper extends BaseMapper<AdminPermission> {
    @Select("SELECT id,name,url,desc_ AS descname FROM admin_permission ")
    List<AdminPermission> selectAll();
}
