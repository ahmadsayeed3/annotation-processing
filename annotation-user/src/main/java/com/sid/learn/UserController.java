package com.sid.learn;

import com.auto.controller.dto.UserDTO;
import com.auto.controller.entity.UserEntity;
import com.auto.controller.mapper.UserMapper;
import com.auto.controller.repository.UserRepository;
import com.sid.learn.annotations.AutoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/user")
    @AutoController(name = "User", tableName = "user")
    public UserDTO getUser(@RequestParam long userId){
        UserEntity userEntity = userRepository.getById(userId);
        return userMapper.entityToDTO(userEntity);
    }
}
