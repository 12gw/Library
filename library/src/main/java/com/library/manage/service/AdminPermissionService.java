package com.library.manage.service;

import com.library.manage.entity.AdminPermission;
import com.library.manage.entity.AdminRole;
import com.library.manage.entity.AdminRolePermission;
import com.library.manage.mapper.AdminPermissionMapper;
import com.library.manage.mapper.AdminRolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminPermissionService {
    @Autowired
    AdminPermissionMapper adminPermissionMapper;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminRolePermissionService adminRolePermissionService;
    @Autowired
    AdminRolePermissionMapper adminRolePermissionMapper;
    @Autowired
    UserService userService;

    public List<AdminPermission> list() {
        return adminPermissionMapper.selectList(null);
    }

    /**
     * 获取全部权限信息
     *
     * @return
     */
    public List<AdminPermission> perms() {
        return adminPermissionMapper.selectAll();
    }

    /**
     * Determine whether client requires permission when requests
     * a certain API.
     *
     * @param requestAPI API requested by client
     * @return true when requestAPI is found in the DB
     */
    public boolean needFilter(String requestAPI) {
        List<AdminPermission> ps = this.list();
        for (AdminPermission p : ps) {
            // match prefix
            if (requestAPI.startsWith(p.getUrl())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据角色id获取角色权限
     *
     * @param rid 角色id
     * @return 权限列表
     */
    public List<AdminPermission> listPermsByRoleId(int rid) {
        List<Integer> pids = adminRolePermissionService.findAllByRid(rid)
                .stream().map(AdminRolePermission::getPid).collect(Collectors.toList());
        if (pids.size() <= 0) {
            return null;
        }
        return adminPermissionMapper.selectBatchIds(pids);
    }

    /**
     * 根据登录名 获取该用户的权限url
     *
     * @param username 用户名
     * @return 权限url集合
     */
    public Set<String> listPermissionURLsByUser(String username) {
        List<Integer> rids = adminRoleService.listRolesByUser(username)
                .stream().map(AdminRole::getId).collect(Collectors.toList());
        List<Integer> pids = adminRolePermissionMapper.selectBatchRids(rids);

        List<AdminPermission> perms = adminPermissionMapper.selectBatchIds(pids);

        Set<String> URLs = perms.stream().map(AdminPermission::getUrl).collect(Collectors.toSet());

        return URLs;
    }
}
