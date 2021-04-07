package com.library.manage.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.library.manage.model.dto.UserDTO;
import com.library.manage.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT u.id,u.name,u.username,u.phone,u.email,u.enabled FROM user AS u ")
    List<UserDTO> selectUserList();
}
