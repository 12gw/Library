package com.library.manage.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.library.manage.mapper.AdminRolePermissionMapper;
import com.library.manage.entity.AdminPermission;
import com.library.manage.entity.AdminRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evan
 * @date 2019/11
 */
@Service
public class AdminRolePermissionService {
    @Autowired
    AdminRolePermissionMapper adminRolePermissionMapper;

    List<AdminRolePermission> findAllByRid(int rid) {
        return adminRolePermissionMapper.selectList(Wrappers.<AdminRolePermission>lambdaQuery().eq(AdminRolePermission::getRid, rid));
    }

    @Transactional
    public void savePermChanges(int rid, List<AdminPermission> perms) {
        adminRolePermissionMapper.delete(Wrappers.<AdminRolePermission>lambdaQuery().eq(AdminRolePermission::getRid, rid));
        List<AdminRolePermission> rps = new ArrayList<>();
        perms.forEach(p -> {
            AdminRolePermission rp = new AdminRolePermission();
            rp.setRid(rid);
            rp.setPid(p.getId());
            rps.add(rp);
        });
        adminRolePermissionMapper.insertBatch(rps);
    }
}
