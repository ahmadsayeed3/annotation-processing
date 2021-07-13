package com.baeldung.annotation.processor.creator;

import lombok.Data;

import java.util.List;

@Data
public class CustomAnnotation {
    private String name;
    List<AnnotationParameter> annotationParameters;
}
