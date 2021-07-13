package com.sid.learn.creator;

import lombok.Data;

import java.util.List;

@Data
public class CustomAnnotation {
    private String name;
    List<AnnotationParameter> annotationParameters;
}
