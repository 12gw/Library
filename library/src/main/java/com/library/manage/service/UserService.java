package com.library.manage.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.library.manage.entity.AdminRole;
import com.library.manage.entity.User;
import com.library.manage.mapper.UserMapper;
import com.library.manage.model.dto.UserDTO;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userDAO;
    @Autowired
    private AdminRoleService adminRoleService;
    @Autowired
    private AdminUserRoleService adminUserRoleService;

    /**
     * 用户列表查询
     *
     * @return
     */
    public List<UserDTO> list() {
        List<UserDTO> userDTOS = userDAO.selectUserList();
        userDTOS.forEach(u -> {
            List<AdminRole> roles = adminRoleService.listRolesByUser(u.getUsername());
            u.setRoles(roles);
        });

        return userDTOS;
    }

    private boolean isExist(String username) {
        User user = userDAO.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        return null != user;
    }

    public User findByUsername(String username) {
        return userDAO.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    public User get(String username, String password) {
        return userDAO.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username).eq(User::getPassword, password));
    }

    public int register(User user) {
        user.setEnabled(true);

        if (StrUtil.isEmpty(user.getUsername()) || StrUtil.isEmpty(user.getPassword())) {
            return 0;
        }

        boolean exist = isExist(user.getUsername());

        if (exist) {
            return 2;
        }

        // 默认生成 16 位盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", user.getPassword(), salt, times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);

        return userDAO.insert(user);
    }

    public void updateUserStatus(User user) {
        User userInDB = this.findByUsername(user.getUsername());
        userInDB.setEnabled(user.isEnabled());
//        userDAO.update(userInDB);
    }

    public int resetPassword(User user) {
        User userInDB = this.findByUsername(user.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        userInDB.setSalt(salt);
        String encodedPassword = new SimpleHash("md5", "123", salt, times).toString();
        userInDB.setPassword(encodedPassword);
        return userDAO.updateById(userInDB);
    }

    public void editUser(User user) {
        User userInDB = this.findByUsername(user.getUsername());
        userInDB.setName(user.getName());
        userInDB.setPhone(user.getPhone());
        userInDB.setEmail(user.getEmail());
        userDAO.updateById(userInDB);
        adminUserRoleService.saveRoleChanges(userInDB.getId(), user.getRoles());
    }

    public void deleteById(int id) {
        userDAO.deleteById(id);
    }

    //    更改用户信息
    public void upUser(User user) {
        user = this.findByUsername(user.getUsername());
        user.setLevel(user.getLevel() + 1);
        userDAO.updateById(user);
    }

    public int getUserRole(String username) {
        if (StrUtil.isNotBlank(username)) {
            User user = this.findByUsername(username);
            if (null != user) {
                return adminUserRoleService.getRole(user.getId());
            }
        }
        return -1;
    }

    /**
     * 根据用户id 获取用户名
     */
    public String getUserName(Integer userId) {
        return userDAO.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId, userId)).getUsername();
    }
}
