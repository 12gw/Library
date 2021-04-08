package com.library.manage.service;

import com.library.manage.entity.AdminMenu;
import com.library.manage.entity.AdminRoleMenu;
import com.library.manage.entity.User;
import com.library.manage.mapper.AdminMenuMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminMenuService {
    @Autowired
    AdminMenuMapper adminMenuMapper;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminRoleMenuService adminRoleMenuService;

//    public List<AdminMenu> getAllByParentId(int parentId) {
//        return adminMenuMapper.selectList(Wrappers.<AdminMenu>lambdaQuery().eq(AdminMenu::getParentId, parentId));
//    }

    /**
     * 获取当前用户的菜单
     *
     * @return 菜单集合
     */
    public List<AdminMenu> getMenusByCurrentUser() {
        // Get current user in DB.
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.findByUsername(username);

        // Get roles' ids of current user.
        List<Integer> rids = adminUserRoleService.listAllByUid(user.getId());

        // Get menu items of these roles.
        List<Integer> menuIds = adminRoleMenuService.findAllByRid(rids);
        List<AdminMenu> menus = adminMenuMapper.selectBatchIds(menuIds);
        List<AdminMenu> menuDTOList = menus.stream().filter(m -> m.getParentId() == 0).collect(Collectors.toList());
        for (AdminMenu level1Menu : menuDTOList) {
            level1Menu.setChildren(getChildren(level1Menu, menus));
        }

        // Adjust the structure of the menu.
//        handleMenus(menus);
        return menuDTOList;
    }

    /**
     * 获取角色的菜单
     *
     * @param rid 角色id
     * @return 菜单集合
     */
    public List<AdminMenu> getMenusByRoleId(int rid) {
        List<Integer> menuIds = adminRoleMenuService.findAllByRid(rid)
                .stream().map(AdminRoleMenu::getMid).collect(Collectors.toList());
        if (menuIds.size() <= 0) {
            return null;
        }
        List<AdminMenu> menus = adminMenuMapper.selectBatchIds(menuIds);
        List<AdminMenu> menuDTOList = menus.stream().filter(m -> m.getParentId() == 0).collect(Collectors.toList());
        for (AdminMenu level1Menu : menuDTOList) {
            level1Menu.setChildren(getChildren(level1Menu, menus));
        }
//        handleMenus(menus);
        return menuDTOList;
    }

    /**
     * Adjust the Structure of the menu.
     *
     * @param menus Menu items list without structure
     */
//    private void handleMenus(List<AdminMenu> menus) {
//        menus.forEach(m -> {
//            List<AdminMenu> children = getAllByParentId(m.getId());
//            m.setChildren(children);
//        });
//
//        menus.removeIf(m -> m.getParentId() != 0);
//    }

    /**
     * 获取子菜单
     *
     * @param level1Menu 一级菜单
     * @param menus      全部菜单集合
     */
    private List<AdminMenu> getChildren(AdminMenu level1Menu, List<AdminMenu> menus) {
        List<AdminMenu> children = new ArrayList<>();
        for (AdminMenu a : menus) {
            if (a.getParentId() == level1Menu.getId()) {
                a.setChildren(getChildren(a, menus));
                children.add(a);
            }
        }
        return children;
    }

}
