package com.library.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.library.manage.entity.AdminRoleMenu;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Evan
 * @date 2019/11
 */
@Repository
public interface AdminRoleMenuMapper extends BaseMapper<AdminRoleMenu> {
    @Select("<script>SELECT * FROM admin_role_menu WHERE rid = <foreach collection ='list' item='rms' separator =','>#{rms}</foreach ></script>")
    List<AdminRoleMenu> findAllByRid(List<Integer> rids);

    @Insert("<script>INSERT INTO admin_role_menu(rid,mid) " +
            "VALUES<foreach collection ='list' item='rms' separator =','>(#{rms.rid}, #{rms.mid}) </foreach ></script>")
    void saveAll(@Param("rms") List<AdminRoleMenu> rms);
}
