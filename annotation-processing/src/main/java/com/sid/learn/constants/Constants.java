package com.sid.learn.constants;

import com.sid.learn.creator.AnnotationParameter;
import com.sid.learn.creator.CustomAnnotation;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static String DEFAULT_PACKAGE = "com.auto.controller";

    /*=== Entity ============================*/
    public static String ENTITY_PACKAGE = DEFAULT_PACKAGE + ".entity";
    public static String SUFFIX_ENTITY = "Entity";

    public static List<String> ENTITY_CLASS_IMPORTS = Arrays.asList("javax.persistence.Entity",
            "javax.persistence.Table",
            "lombok.Data",
            "lombok.AllArgsConstructor",
            "lombok.NoArgsConstructor",
            "java.util.Date",
            "javax.persistence.*");

    public static List<CustomAnnotation> ENTITY_CLASS_ANNOTATIONS = Arrays.asList(
            new CustomAnnotation("Entity", null),
            new CustomAnnotation("Data", null),
            new CustomAnnotation("AllArgsConstructor", null));

    /*=== DTO ============================*/
    public static String DTO_PACKAGE = DEFAULT_PACKAGE +".dto";
    public static String SUFFIX_DTO = "DTO";
    public static List<String> DTO_CLASS_IMPORTS = Arrays.asList("lombok.Data",
            "lombok.AllArgsConstructor",
            "lombok.NoArgsConstructor",
            "java.util.Date");

    public static List<CustomAnnotation> DTO_CLASS_ANNOTATIONS = Arrays.asList(new CustomAnnotation("Data", null),
            new CustomAnnotation("AllArgsConstructor", null));


    /*=== Mapper ============================*/
    public static String MAPPER_PACKAGE = DEFAULT_PACKAGE + ".mapper";
    public static String SUFFIX_MAPPER = "Mapper";
    public static List<CustomAnnotation> MAPPER_INTERFACE_IMPORTS = Arrays.asList(
            new CustomAnnotation("Mapper", Arrays.asList(new AnnotationParameter("componentModel", "\"spring\""))));

    /*=== Mapper ============================*/
    public static String REPOSITORY_PACKAGE = DEFAULT_PACKAGE + ".repository";
    public static String SUFFIX_REPOSITORY = "Repository";
    public static  List<String> REPOSITORY_INTERFACE_IMPORTS = Arrays.asList("org.springframework.stereotype.Repository",
            "org.springframework.data.jpa.repository.JpaRepository");

    public static List<CustomAnnotation> REPOSITORY_INTERFACE_ANNOTATIONS = Arrays.asList(new CustomAnnotation("Repository", null));

}
