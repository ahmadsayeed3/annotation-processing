package com.sid.learn;

import com.auto.controller.dto.UserDTO;
import com.auto.controller.entity.UserEntity;
import com.auto.controller.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @AutoController(name = "User", tableName = "user")
    public UserDTO getUser(@RequestParam long userId){
        UserEntity userEntity = new UserEntity();
        return userMapper.entityToDTO(userEntity);
    }

}
