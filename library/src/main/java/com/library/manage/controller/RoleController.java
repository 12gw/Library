package com.library.manage.controller;

import com.library.manage.config.result.Result;
import com.library.manage.config.result.ResultFactory;
import com.library.manage.entity.AdminRole;
import com.library.manage.service.AdminPermissionService;
import com.library.manage.service.AdminRoleMenuService;
import com.library.manage.service.AdminRolePermissionService;
import com.library.manage.service.AdminRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags ="角色管理")
public class RoleController {
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminPermissionService adminPermissionService;
    @Autowired
    AdminRolePermissionService adminRolePermissionService;
    @Autowired
    AdminRoleMenuService adminRoleMenuService;

    @ApiOperation(value = "查询角色列表")
    @GetMapping("/api/admin/role")
    public Result listRoles() {
        return ResultFactory.buildSuccessResult(adminRoleService.listWithPermsAndMenus());
    }

    @PutMapping("/api/admin/role/status")
    @ApiOperation(value = "更新角色状态")
    public Result updateRoleStatus(@RequestBody AdminRole requestRole) {
        int adminRole = adminRoleService.updateRoleStatus(requestRole);
//        String message = "用户" + adminRole.getNameZh() + "状态更新成功";
        return ResultFactory.buildSuccessResult("用户状态更新成功");
    }

    @PutMapping("/api/admin/role")
    @ApiOperation(value = "修改角色信息")
    public Result editRole(@RequestBody AdminRole requestRole) {
        adminRoleService.addOrUpdate(requestRole);
        adminRolePermissionService.savePermChanges(requestRole.getId(), requestRole.getPerms());
        adminRoleMenuService.updateRoleMenu(requestRole.getId(), requestRole.getMenuIds());
        return ResultFactory.buildSuccessResult("修改角色信息成功");
    }


    @PostMapping("/api/admin/role")
    @ApiOperation(value = "修改用户角色")
    public Result addRole(@RequestBody AdminRole requestRole) {
        adminRoleService.editRole(requestRole);
        return ResultFactory.buildSuccessResult("修改用户成功");
    }

    @GetMapping("/api/admin/role/perm")
    @ApiOperation(value = "查询角色权限列表")
    public Result listPerms() {
        return ResultFactory.buildSuccessResult(adminPermissionService.perms());
    }

//    @PutMapping("/api/admin/role/menu")
//    public Result updateRoleMenu(Integer rid, Map<String, List<Integer>> menusIds) {
////        adminRoleMenuService.updateRoleMenu(rid, menusIds);
//        return ResultFactory.buildSuccessResult("更新成功");
//    }

    @GetMapping("/api/admin/roleList")
    @ApiOperation(value = "查询角色全部信息列表")
    public Result listRole() {
        return ResultFactory.buildSuccessResult(adminRoleService.listWithPermsAndMenus());
    }
}
