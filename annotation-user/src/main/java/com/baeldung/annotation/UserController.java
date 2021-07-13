package com.baeldung.annotation;

import com.auto.controller.entity.UserEntity;
import com.baeldung.annotation.processor.AutoController;

public class UserController {

    @AutoController(name = "User", tableName = "user")
    public String getUser(){
        UserEntity userEntity = new UserEntity();
        return "";
    }

}
