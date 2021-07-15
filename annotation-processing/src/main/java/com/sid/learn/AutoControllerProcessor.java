package com.sid.learn;

import com.sid.learn.creator.*;
import com.google.auto.service.AutoService;
import com.sid.learn.creator.database.TableMetaData;

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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("com.sid.learn.AutoController")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class AutoControllerProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private static String DEFAULT_PACKAGE = "com.auto.controller";

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
                        try {
                            generateClassFileAndSource(autoController);
                        } catch (SQLException throwable) {
                            throwable.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return false;
    }

    private void generateClassFileAndSource(AutoController autoController) throws SQLException, ClassNotFoundException {
        generateEntity(autoController.name(), autoController.tableName());
        generateDTO(autoController.name());
        generateMapper(autoController.name());
    }

    private void generateEntity(String className, String tableName) throws SQLException, ClassNotFoundException {
        List<String> imports = Arrays.asList("javax.persistence.Entity",
                "javax.persistence.Table","lombok.Data", "lombok.AllArgsConstructor", "lombok.NoArgsConstructor");
        List<CustomAnnotation> customAnnotations = Arrays.asList(
                new CustomAnnotation("Entity", null),
                new CustomAnnotation("Table", Arrays.asList(new AnnotationParameter("name", "\"user\""))),
                new CustomAnnotation("Data", null),
                new CustomAnnotation("AllArgsConstructor", null));

        //TableMetaData tableMetaData = new TableMetaData(tableName);

        String entityPackage = DEFAULT_PACKAGE + ".entity";
        String entityClassName = className + "Entity";
        String fullClassName = entityPackage + "." + entityClassName;
        CustomClass customClass = CustomClass.builder()
                .packageName(entityPackage)
                .imports(imports)
                .classType("class")
                .className(entityClassName)
                .customAnnotations(customAnnotations)
                .build();
        ClassStringMaker classStringMaker = new ClassStringMaker(customClass);
        String classString = classStringMaker.classAsString();
        generateClassFileAndSource(fullClassName, classString);
    }

    private void generateDTO(String className){
        List<String> imports = Arrays.asList("lombok.Data", "lombok.AllArgsConstructor", "lombok.NoArgsConstructor");
        List<CustomAnnotation> classAnnotations = Arrays.asList(new CustomAnnotation("Data", null));

        String dtoClassName = className + "DTO";
        String dtoPackage = DEFAULT_PACKAGE + ".dto";
        String fullClassName = dtoPackage + "." + dtoClassName;
        CustomClass customClass = CustomClass.builder().packageName(dtoPackage)
                .imports(imports)
                .classType("class")
                .className(dtoClassName)
                .customAnnotations(classAnnotations)
                .build();

        String classAsString = new ClassStringMaker(customClass).classAsString();
        generateClassFileAndSource(fullClassName, classAsString);
    }

    private void generateMapper(String className){
        String dtoClassImport = "com.auto.controller.dto." + className + "DTO";
        String entityClassImport = "com.auto.controller.entity." + className + "Entity";
        List<String> imports = Arrays.asList("org.mapstruct.*", dtoClassImport, entityClassImport);

        List<CustomAnnotation> customAnnotations = Arrays.asList(
                new CustomAnnotation("Mapper", Arrays.asList(new AnnotationParameter("componentModel", "\"spring\""))));

        String mapperClassName = className + "Mapper";
        String mapperPackage = DEFAULT_PACKAGE + "." + "mapper";
        String fullClassName = mapperPackage + "." + mapperClassName;

        CustomMethod dtoToEntityMethod = new CustomMethod();
        dtoToEntityMethod.setModifier("public");
        dtoToEntityMethod.setReturnType(className + "Entity");
        dtoToEntityMethod.setName("dtoToEntity");
        List<MethodParameter> methodParameters = Arrays.asList(new MethodParameter(className + "DTO", Introspector.decapitalize(className + "DTO"), null));
        dtoToEntityMethod.setMethodParameters(methodParameters);

        CustomMethod entityToDtoMethod = new CustomMethod();
        entityToDtoMethod.setModifier("public");
        entityToDtoMethod.setReturnType(className + "DTO");
        entityToDtoMethod.setName("entityToDTO");
        List<MethodParameter> entityToDtoParameters = Arrays.asList(new MethodParameter(className + "Entity", Introspector.decapitalize(className + "Entity"), null));
        entityToDtoMethod.setMethodParameters(entityToDtoParameters);

        CustomClass customClass = CustomClass.builder()
                .packageName(mapperPackage)
                .imports(imports)
                .classType("interface")
                .className(mapperClassName)
                .customAnnotations(customAnnotations)
                .customMethods(Arrays.asList(dtoToEntityMethod, entityToDtoMethod))
                .build();

        String classAsString = new ClassStringMaker(customClass).classAsString();
        generateClassFileAndSource(fullClassName, classAsString);

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
