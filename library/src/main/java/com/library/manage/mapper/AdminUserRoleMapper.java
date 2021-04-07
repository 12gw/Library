package com.library.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.library.manage.entity.AdminUserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserRoleMapper extends BaseMapper<AdminUserRole> {
    @Insert("<script>INSERT INTO admin_user_role(uid,rid) " +
            "VALUES<foreach collection ='list' item='list' separator =','>(#{list.uid}, #{list.rid})" +
            " </foreach ></script>")
    int insertList(@Param("list") List<AdminUserRole> list);
}
