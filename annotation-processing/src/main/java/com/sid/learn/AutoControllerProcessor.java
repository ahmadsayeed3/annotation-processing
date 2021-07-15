package com.sid.learn;

import com.sid.learn.annotations.AutoController;
import com.sid.learn.creator.*;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import java.beans.Introspector;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("com.sid.learn.annotations.AutoController")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class AutoControllerProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private String DEFAULT_PACKAGE = "com.auto.controller";

    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(AutoController.class)
                .forEach(element -> {
                    if(element.getKind() == ElementKind.METHOD){
                        AutoController autoController = element.getAnnotation(AutoController.class);
                        String name = autoController.name();
                        generateClassFileAndSource(autoController);
                    }
                });
        return false;
    }

    private void generateClassFileAndSource(AutoController autoController) {
        generateEntity(autoController.name(), autoController.tableName());
        generateDTO(autoController.name());
        generateMapper(autoController.name());
        generateRepository(autoController.name());
    }

    private void generateEntity(String className, String tableName) {

        List<CustomAnnotation> customAnnotations = new ArrayList<>(Constants.ENTITY_CLASS_ANNOTATIONS);
        customAnnotations.add(new CustomAnnotation("Table", Arrays.asList(new AnnotationParameter("name", "\""+tableName+"\""))));

        //TableMetaData tableMetaData = new TableMetaData(tableName);
        String entityClassName = className + Constants.SUFFIX_ENTITY;
        String fullClassName = Constants.ENTITY_PACKAGE + "." + entityClassName;
        CustomClass customClass = CustomClass.builder()
                .packageName(Constants.ENTITY_PACKAGE)
                .imports(Constants.ENTITY_CLASS_IMPORTS)
                .classType("class")
                .className(entityClassName)
                .customAnnotations(customAnnotations)
                .build();

        ClassStringMaker classStringMaker = new ClassStringMaker(customClass);
        String classString = classStringMaker.classAsString();
        generateClassFileAndSource(fullClassName, classString);
    }

    private void generateDTO(String className){

        String dtoClassName = className + Constants.SUFFIX_DTO;
        String fullClassName = Constants.DTO_PACKAGE + "." + dtoClassName;
        CustomClass customClass = CustomClass.builder().packageName(Constants.DTO_PACKAGE)
                .imports(Constants.DTO_CLASS_IMPORTS)
                .classType("class")
                .className(dtoClassName)
                .customAnnotations(Constants.DTO_CLASS_ANNOTATIONS)
                .build();

        String classAsString = new ClassStringMaker(customClass).classAsString();
        generateClassFileAndSource(fullClassName, classAsString);
    }

    private void generateMapper(String className){
        String dtoClassImport = "com.auto.controller.dto." + className + "DTO";
        String entityClassImport = "com.auto.controller.entity." + className + "Entity";
        List<String> imports = Arrays.asList("org.mapstruct.*", dtoClassImport, entityClassImport);

        String entityClassName = className + Constants.SUFFIX_ENTITY;
        String dtoClassName = className + Constants.SUFFIX_DTO;
        String mapperClassName = className + Constants.SUFFIX_MAPPER;
        String fullClassName = Constants.MAPPER_PACKAGE + "." + mapperClassName;

        List<MethodParameter> methodParameters = Arrays.asList(new MethodParameter(dtoClassName, Introspector.decapitalize(dtoClassName), null));
        CustomMethod dtoToEntityMethod = buildCustomMethod("public", entityClassName, "dtoToEntity", methodParameters);

        List<MethodParameter> entityToDtoParameters = Arrays.asList(new MethodParameter(entityClassName, Introspector.decapitalize(entityClassName), null));
        CustomMethod entityToDtoMethod = buildCustomMethod("public", dtoClassName, "entityToDTO", entityToDtoParameters);

        CustomClass customClass = CustomClass.builder()
                .packageName(Constants.MAPPER_PACKAGE)
                .imports(imports)
                .classType("interface")
                .className(mapperClassName)
                .customAnnotations(Constants.MAPPER_INTERFACE_IMPORTS)
                .customMethods(Arrays.asList(dtoToEntityMethod, entityToDtoMethod))
                .build();

        String classAsString = new ClassStringMaker(customClass).classAsString();
        generateClassFileAndSource(fullClassName, classAsString);

    }

    private void generateRepository(String className){
        String repositoryClassName = className + Constants.SUFFIX_REPOSITORY;
        String fullClassName = Constants.REPOSITORY_PACKAGE + "." + repositoryClassName;
        String entityClassName = className + Constants.SUFFIX_ENTITY;

        List<String> imports = new ArrayList<>(Constants.REPOSITORY_INTERFACE_IMPORTS);
        imports.add("com.auto.controller.entity." + entityClassName);

        CustomClass customClass = CustomClass.builder().packageName(Constants.REPOSITORY_PACKAGE)
                .imports(imports)
                .classType("interface")
                .className(repositoryClassName)
                .customClassExtends(new CustomClassExtends("JpaRepository", Arrays.asList(entityClassName, "Long")))
                .customAnnotations(Constants.REPOSITORY_INTERFACE_ANNOTATIONS)
                .build();

        String classAsString = new ClassStringMaker(customClass).classAsString();
        generateClassFileAndSource(fullClassName, classAsString);
    }

    private CustomClass buildCustomClass(String packageName, List<String> imports, String classType, String className, CustomClassExtends customClassExtends, List<CustomAnnotation> customAnnotations){
        return CustomClass.builder().packageName(packageName)
                .imports(imports)
                .classType(classType)
                .className(className)
                .customClassExtends(customClassExtends)
                .customAnnotations(customAnnotations)
                .build();
    }

    private CustomMethod buildCustomMethod(String modifier, String returnType, String methodName, List<MethodParameter> methodParameters){
        CustomMethod customMethod = new CustomMethod();
        customMethod.setModifier(modifier);
        customMethod.setReturnType(returnType);
        customMethod.setName(methodName);
        customMethod.setMethodParameters(methodParameters);
        return customMethod;
    }

    private void generateClassFileAndSource(String fullClassName, String classText){
        try {
            JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(fullClassName);
            PrintWriter out = new PrintWriter(builderFile.openWriter());
            out.print(classText);
            out.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
