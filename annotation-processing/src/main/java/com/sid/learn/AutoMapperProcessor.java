package com.sid.learn;

import com.google.auto.service.AutoService;
import com.sid.learn.annotations.AutoMapper;
import com.sid.learn.constants.Constants;
import com.sid.learn.creator.ClassStringMaker;
import com.sid.learn.creator.CustomClass;
import com.sid.learn.creator.CustomMethod;
import com.sid.learn.creator.MethodParameter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import java.beans.Introspector;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("com.sid.learn.annotations.AutoMapper")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class AutoMapperProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Types typeUtils;
    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = this.processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(AutoMapper.class)
                .forEach(element -> {
                    AutoMapper autoMapper = element.getAnnotation(AutoMapper.class);
                    List<String> classes = new ArrayList<>(2);

                    List<? extends TypeMirror> types = APUtils.getTypeMirrorFromAnnotationValue(() -> autoMapper.classes());
                    types.forEach(typeMirror -> {
                        TypeElement typeElement = (TypeElement) typeUtils.asElement(typeMirror);
                        classes.add(typeElement.getQualifiedName().toString());
                    });
                    generateMapper(autoMapper.name(), classes);
                });
        return false;
    }

    private void generateMapper(String mapperClassName, List<String> classes){
        String fromClassImport = classes.get(0);
        String toClassImport = classes.get(1);
        List<String> imports = Arrays.asList("org.mapstruct.*",
                fromClassImport, toClassImport,
                "org.springframework.stereotype.Service",
                "java.util.List");

        String fromClassName = fromClassImport.substring(fromClassImport.lastIndexOf(".")+1);
        String toClassName = toClassImport.substring(toClassImport.lastIndexOf(".")+1);
        String fullClassName = Constants.MAPPER_PACKAGE + "." + mapperClassName;


        List<MethodParameter> methodParameters = Arrays.asList(new MethodParameter(fromClassName, Introspector.decapitalize(fromClassName), null));
        String fromClassToClassMethodName = Introspector.decapitalize(fromClassName) + "To" + toClassName;
        CustomMethod fromToCustomMethod = buildCustomMethod("public", toClassName, fromClassToClassMethodName, methodParameters);

        List<MethodParameter> entityToDtoParameters = Arrays.asList(new MethodParameter(toClassName, Introspector.decapitalize(toClassName), null));
        String toClassFromClassMethodName =  Introspector.decapitalize(toClassName) + "To" + fromClassName;
        CustomMethod toFromCustomMethod = buildCustomMethod("public", fromClassName, toClassFromClassMethodName, entityToDtoParameters);

        List<MethodParameter> methodParametersList = Arrays.asList(new MethodParameter("List<"+ fromClassName +">", Introspector.decapitalize(fromClassName), null));
        String fromClassToClassMethodNameList = Introspector.decapitalize(fromClassName) + "To" + toClassName + "List";
        CustomMethod fromToCustomMethodList = buildCustomMethod("public", "List<" + toClassName +">" , fromClassToClassMethodNameList, methodParametersList);

        List<MethodParameter> entityToDtoParametersList = Arrays.asList(new MethodParameter("List<" + toClassName + ">", Introspector.decapitalize(toClassName), null));
        String toClassFromClassMethodNameList =  Introspector.decapitalize(toClassName) + "To" + fromClassName+"List";
        CustomMethod toFromCustomMethodList = buildCustomMethod("public", "List<" + fromClassName + ">", toClassFromClassMethodNameList, entityToDtoParametersList);

        CustomClass customClass = CustomClass.builder()
                .packageName(Constants.MAPPER_PACKAGE)
                .imports(imports)
                .classType("interface")
                .className(mapperClassName)
                .customAnnotations(Constants.MAPPER_INTERFACE_ANNOTATIONS)
                .customMethods(Arrays.asList(fromToCustomMethod, toFromCustomMethod, fromToCustomMethodList, toFromCustomMethodList))
                .build();

        String classAsString = new ClassStringMaker(customClass).classAsString();
        generateClassFileAndSource(fullClassName, classAsString);
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

    static class APUtils {

        @FunctionalInterface
        public interface GetClassValue {
            void execute() throws MirroredTypeException, MirroredTypesException;
        }

        public static List<? extends TypeMirror> getTypeMirrorFromAnnotationValue(GetClassValue c) {
            try {
                c.execute();
            } catch (MirroredTypesException ex) {
                return ex.getTypeMirrors();
            }
            return null;
        }
    }
}
