package com.sid.learn.mapper;

import com.sid.learn.annotations.AutoMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @GetMapping("/api/employee")
    @AutoMapper(name="EmployeeMapper", classes = {EmployeeDTO.class, EmployeeEntity.class})
    public EmployeeDTO getEmployee(@RequestParam long id){
        EmployeeEntity employeeEntity = new EmployeeEntity();
        return null;
    }

}
