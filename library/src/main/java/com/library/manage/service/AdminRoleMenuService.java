package com.library.manage.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.library.manage.entity.AdminMenu;
import com.library.manage.entity.AdminRoleMenu;
import com.library.manage.mapper.AdminRoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdminRoleMenuService {
    @Autowired
    AdminRoleMenuMapper adminRoleMenuMapper;

    public List<AdminRoleMenu> findAllByRid(int rid) {
        return adminRoleMenuMapper.selectList(Wrappers.<AdminRoleMenu>lambdaQuery().eq(AdminRoleMenu::getRid, rid));
    }

    public List<Integer> findAllByRid(List<Integer> rids) {
        return adminRoleMenuMapper.findAllByRid(rids);
    }

    public void save(AdminRoleMenu rm) {
        adminRoleMenuMapper.insert(rm);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateRoleMenu(int rid, List<Integer> menusIds) {
        adminRoleMenuMapper.deleteById(rid);
        List<AdminRoleMenu> rms = new ArrayList<>();
        for (Integer mid : menusIds) {
            AdminRoleMenu rm = new AdminRoleMenu();
            rm.setMid(mid);
            rm.setRid(rid);
            rms.add(rm);
        }
        adminRoleMenuMapper.saveAll(rms);
    }
}
