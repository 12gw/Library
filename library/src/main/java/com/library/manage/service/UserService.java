package com.library.manage.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.library.manage.dto.UserDTO;
import com.library.manage.entity.AdminRole;
import com.library.manage.entity.User;
import com.library.manage.mapper.UserMapper;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Evan
 * @date 2019/4
 */
@Service
public class UserService {
    @Autowired
    UserMapper userDAO;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminUserRoleService adminUserRoleService;

    /**
     * 用户列表查询
     *
     * @return
     */
    public List<UserDTO> list() {
        List<User> users = userDAO.selectList(null);
        List<UserDTO> userDTOS = users.stream().map(user -> (UserDTO) new UserDTO().convertFrom(user)).collect(Collectors.toList());
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
        String username = user.getUsername();
        String name = user.getName();
        String phone = user.getPhone();
        String email = user.getEmail();
        String password = user.getPassword();

        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        phone = HtmlUtils.htmlEscape(phone);
        user.setPhone(phone);
        email = HtmlUtils.htmlEscape(email);
        user.setEmail(email);
        user.setEnabled(true);

        if (username.equals("") || password.equals("")) {
            return 0;
        }

        boolean exist = isExist(username);

        if (exist) {
            return 2;
        }

        // 默认生成 16 位盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);

        userDAO.insert(user);

        return 1;
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
}
