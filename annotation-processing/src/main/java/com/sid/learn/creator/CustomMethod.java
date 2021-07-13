package com.sid.learn.creator;

import lombok.Data;

import java.util.List;

@Data
public class CustomMethod {

    private String modifier;
    private String returnType;
    private String name;
    private List<MethodParameter> methodParameters;
    private List<CustomAnnotation> customAnnotations;

}
