package com.sid.learn.creator;

public class ClassStringMaker {

    private CustomClass customClass;
    private StringBuilder classString;

    public ClassStringMaker(CustomClass customClass){
        this.customClass = customClass;
        this.classString = new StringBuilder();
    }

    public String make(){
        setPackage();
        setImports();
        setClassName();
        setCloseClass();
        return classString.toString();
    }

    private void setPackage(){
        classString.append("package ").append(customClass.getPackageName()).append(";").append("\n\n");
    }

    private void setImports(){
        customClass.getImports().forEach(importClassPath -> {
            classString.append("import ").append(importClassPath).append(";").append("\n");
        });
        classString.append("\n");
    }

    private void setClassName(){
        if(customClass.getCustomAnnotations() != null){
            customClass.getCustomAnnotations().forEach(customAnnotation -> {
                classString.append("@").append(customAnnotation.getName());
                if(customAnnotation.getAnnotationParameters() != null){
                    classString.append("(");
                    customAnnotation.getAnnotationParameters().forEach(annotationParameter -> {
                        classString.append(annotationParameter.getName()).append(" = ")
                                .append(annotationParameter.getValue()).append(",");
                    });
                    classString.replace(classString.lastIndexOf(","), classString.length(), "");
                    classString.append(")");
                }

                classString.append("\n");
            });
        }
        classString.append("public class ").append(customClass.getClassName()).append("{").append("\n");
    }

    private void setCloseClass(){
        classString.append("}").append("\n");
    }

}
