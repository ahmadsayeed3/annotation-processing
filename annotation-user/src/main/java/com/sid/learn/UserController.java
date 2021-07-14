package com.sid.learn;

import com.auto.controller.dto.UserDTO;
import com.auto.controller.entity.UserEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @AutoController(name = "User", tableName = "user")
    public UserDTO getUser(@RequestParam long userId){
        UserEntity userEntity = new UserEntity();
        return null;
    }

}
