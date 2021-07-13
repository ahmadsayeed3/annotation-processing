package com.sid.learn;

import com.auto.controller.entity.UserEntity;

public class UserController {

    @AutoController(name = "User", tableName = "user")
    public String getUser(){
       UserEntity userEntity = new UserEntity();
        return "";
    }

}
