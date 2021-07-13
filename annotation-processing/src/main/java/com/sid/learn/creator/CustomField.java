package com.sid.learn.creator;

import lombok.Data;

import java.util.List;

@Data
public class CustomField {

    private String modifier;
    private String returnType;
    private String name;
    private List<CustomAnnotation> customAnnotations;

}
