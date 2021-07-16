package com.sid.learn;

import com.auto.controller.dto.PersonDTO;
import com.auto.controller.entity.PersonEntity;
import com.auto.controller.mapper.PersonMapper;
import com.auto.controller.repository.PersonRepository;
import com.sid.learn.annotations.AutoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @Autowired
    PersonMapper personMapper;

    @Autowired
    PersonRepository personRepository;

    @AutoController(name = "Person", tableName = "person")
    @GetMapping("/api/person")
    public PersonDTO getPerson(@RequestParam long id){
        PersonEntity personEntity = personRepository.getById(id);
        return personMapper.entityToDTO(personEntity);
    }
}
