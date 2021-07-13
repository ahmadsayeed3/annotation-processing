package com.baeldung.annotation.processor.creator;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CustomClass {

    private String packageName;
    private String className;
    private List<CustomField> classFields;
    private List<CustomMethod> customMethods;
    private List<CustomAnnotation> customAnnotations;
    private List<String> imports;

}
