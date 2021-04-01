package com.library.manage.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.library.manage.entity.AdminPermission;
import com.library.manage.entity.AdminRole;
import com.library.manage.entity.AdminRolePermission;
import com.library.manage.mapper.AdminPermissionMapper;
import com.library.manage.mapper.AdminRolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Evan
 * @date 2019/11
 */
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

    public List<AdminPermission> listPermsByRoleId(int rid) {
        List<Integer> pids = adminRolePermissionService.findAllByRid(rid)
                .stream().map(AdminRolePermission::getPid).collect(Collectors.toList());
        return adminPermissionMapper.selectBatchIds(pids);
    }

    public Set<String> listPermissionURLsByUser(String username) {
        List<Integer> rids = adminRoleService.listRolesByUser(username)
                .stream().map(AdminRole::getId).collect(Collectors.toList());
        List<Integer> pids = new ArrayList<>();
        for (Integer i : rids){
            pids = adminRolePermissionMapper.selectList(Wrappers.<AdminRolePermission>lambdaQuery().eq(AdminRolePermission::getRid,i)).stream().map(AdminRolePermission::getPid).collect(Collectors.toList());
        }

        List<AdminPermission> perms = adminPermissionMapper.selectBatchIds(pids);

        Set<String> URLs = perms.stream().map(AdminPermission::getUrl).collect(Collectors.toSet());

        return URLs;
    }
}
