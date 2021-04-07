package com.library.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.library.manage.entity.AdminRolePermission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRolePermissionMapper extends BaseMapper<AdminRolePermission> {
    @Insert("<script>INSERT INTO admin_role_permission(rid,pid) " +
            "VALUES<foreach collection ='list' item='list' separator =','>(#{list.rid}, #{list.pid}) </foreach ></script>")
    void insertBatch(@Param("list") List<AdminRolePermission> rps);

    @Select("<script>SELECT pid FROM admin_role_permission " +
            "WHERE rid = <foreach collection ='list' item='list' separator =','>#{list} </foreach ></script>")
    List<Integer> selectBatchRids(@Param("list") List<Integer> list);
}
