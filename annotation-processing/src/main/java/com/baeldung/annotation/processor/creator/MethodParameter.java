package com.baeldung.annotation.processor.creator;

import lombok.Data;

import java.util.List;

@Data
public class MethodParameter {

    private String type;
    private String name;
    private List<CustomAnnotation> customAnnotations;

}
