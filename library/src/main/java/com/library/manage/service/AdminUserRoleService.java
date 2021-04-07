package com.library.manage.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.library.manage.entity.AdminRole;
import com.library.manage.entity.AdminUserRole;
import com.library.manage.mapper.AdminUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserRoleService {
    @Autowired
    AdminUserRoleMapper adminUserRoleMapper;

    @Autowired
    UserService userService;

    public List<Integer> listAllByUid(int uid) {
        return adminUserRoleMapper.selectList((Wrappers.<AdminUserRole>lambdaQuery().eq(AdminUserRole::getUid, uid)))
                .stream().map(AdminUserRole::getRid).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveRoleChanges(int uid, List<AdminRole> roles) {
        adminUserRoleMapper.delete(Wrappers.<AdminUserRole>lambdaQuery().eq(AdminUserRole::getUid, uid));
        List<AdminUserRole> urs = new ArrayList<>();
        roles.forEach(r -> {
            AdminUserRole ur = new AdminUserRole();
            ur.setUid(uid);
            ur.setRid(r.getId());
            urs.add(ur);
        });
        adminUserRoleMapper.insertList(urs);
    }

    public int getRole(int uid) {
        return adminUserRoleMapper.selectOne(Wrappers.<AdminUserRole>lambdaQuery().eq(AdminUserRole::getUid, uid)).getRid();
    }
}
