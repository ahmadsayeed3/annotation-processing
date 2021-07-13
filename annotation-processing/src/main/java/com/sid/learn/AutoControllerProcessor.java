package com.sid.learn;

import com.sid.learn.creator.AnnotationParameter;
import com.sid.learn.creator.ClassStringMaker;
import com.sid.learn.creator.CustomAnnotation;
import com.sid.learn.creator.CustomClass;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
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
                        generateClass(name);
                    }
                });
        return false;
    }

    private void generateClass(String className){
        generateEntity(className);
    }

    private void generateEntity(String className){
        List<String> imports = Arrays.asList("javax.persistence.Entity",
                "javax.persistence.Table");
        List<CustomAnnotation> customAnnotations = Arrays.asList(
                new CustomAnnotation("Entity", null),
                new CustomAnnotation("Table", Arrays.asList(new AnnotationParameter("name", "\"user\""))));

        String entityClassName = className + "Entity";
        CustomClass customClass = CustomClass.builder()
                .packageName("com.auto.controller.entity")
                .imports(imports)
                .className(entityClassName)
                .customAnnotations(customAnnotations)
                .build();
        ClassStringMaker classStringMaker = new ClassStringMaker(customClass);
        String classString = classStringMaker.make();
        generateClass(entityClassName, classString);
    }

    private void generateClass(String className, String classText){
        try {
            JavaFileObject builderFile = processingEnv.getFiler().createSourceFile("com.auto.controller.entity." + className);
            PrintWriter out = new PrintWriter(builderFile.openWriter());
            out.print(classText);
            out.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
