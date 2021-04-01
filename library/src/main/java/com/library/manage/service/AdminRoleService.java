package com.library.manage.service;

import com.library.manage.entity.AdminMenu;
import com.library.manage.entity.AdminPermission;
import com.library.manage.entity.AdminRole;
import com.library.manage.entity.AdminUserRole;
import com.library.manage.mapper.AdminRoleMapper;
import com.library.manage.mapper.AdminUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
    AdminMenuService adminMenuService;

    public List<AdminRole> listWithPermsAndMenus() {
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
    }

    public List<AdminRole> roleList() {
        return adminRoleMapper.selectList(null);
    }

    public List<AdminRole> findAll() {
        return adminRoleMapper.selectList(null);
    }


    public void addOrUpdate(AdminRole adminRole) {
        adminRoleMapper.insert(adminRole);
    }

    public List<AdminRole> listRolesByUser(String username) {
        int uid = userService.findByUsername(username).getId();
        List<Integer> rids = adminUserRoleService.listAllByUid(uid)
                .stream().map(AdminUserRole::getRid).collect(Collectors.toList());
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
