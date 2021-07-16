package com.sid.learn.mapper;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class EmployeeEntity {

    private long id;
    private String name;
    private int age;
    private String address;

}
