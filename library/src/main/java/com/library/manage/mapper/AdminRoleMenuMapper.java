package com.library.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.library.manage.entity.AdminRoleMenu;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRoleMenuMapper extends BaseMapper<AdminRoleMenu> {
    @Select("<script>SELECT mid FROM admin_role_menu WHERE rid = <foreach collection ='list' item='rms' separator =','>#{rms}</foreach ></script>")
    List<Integer> findAllByRid(List<Integer> rids);

    @Insert("<script>INSERT INTO admin_role_menu(rid,mid) " +
            "VALUES<foreach collection ='list' item='list' separator =','>(#{list.rid}, #{list.mid}) </foreach ></script>")
    void saveAll(@Param("list") List<AdminRoleMenu> rms);
}
