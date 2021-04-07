package com.library.manage.service;

import com.library.manage.entity.AdminMenu;
import com.library.manage.entity.AdminPermission;
import com.library.manage.entity.AdminRole;
import com.library.manage.mapper.AdminRoleMapper;
import com.library.manage.mapper.AdminUserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@Slf4j
public class AdminRoleService {
    @Autowired
    AdminRoleMapper adminRoleMapper;
    @Autowired
    AdminUserRoleMapper adminUserRoleMapper;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminPermissionService adminPermissionService;
    @Autowired
    AdminRolePermissionService adminRolePermissionService;
    @Autowired
    AdminRoleMenuService adminRoleMenuService;
    @Autowired
    AdminMenuService adminMenuService;

    public List<AdminRole> listWithPermsAndMenus() {
        try {
            List<AdminRole> roles = adminRoleMapper.selectList(null);
            List<AdminPermission> perms;
            List<AdminMenu> menus;
            for (AdminRole role : roles) {
                perms = adminPermissionService.listPermsByRoleId(role.getId());
                menus = adminMenuService.getMenusByRoleId(role.getId());
                role.setPerms(perms);
                role.setMenus(menus);
            }
            return roles;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return null;
    }

    public List<AdminRole> roleList() {
        return adminRoleMapper.selectList(null);
    }


    public void addOrUpdate(AdminRole adminRole) {
        if (adminRole.getId() != null){
            adminRoleMapper.updateById(adminRole);
        }else {
            adminRoleMapper.insert(adminRole);
        }
    }

    public List<AdminRole> listRolesByUser(String username) {
        int uid = userService.findByUsername(username).getId();
        List<Integer> rids = adminUserRoleService.listAllByUid(uid);
        return adminRoleMapper.selectBatchIds(rids);
    }

    public int updateRoleStatus(AdminRole role) {
        AdminRole roleInDB = adminRoleMapper.selectById(role.getId());
        roleInDB.setEnabled(role.isEnabled());
        return adminRoleMapper.updateById(roleInDB);
    }

    public void editRole(@RequestBody AdminRole role) {
        adminRoleMapper.updateById(role);
        adminRolePermissionService.savePermChanges(role.getId(), role.getPerms());
    }
}
