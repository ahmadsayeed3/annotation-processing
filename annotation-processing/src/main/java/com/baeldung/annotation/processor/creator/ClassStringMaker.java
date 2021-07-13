package com.baeldung.annotation.processor.creator;

public class ClassStringMaker {

    private CustomClass customClass;
    private StringBuilder classString;

    public ClassStringMaker(CustomClass customClass){
        this.customClass = customClass;
        this.classString = new StringBuilder();
    }

    public String make(){
        setPackage();
        //setImports();
        setClassName();
        setCloseClass();
        return classString.toString();
    }

    private void setPackage(){
        classString.append("package ").append(customClass.getPackageName()).append(";");
    }

    private void setImports(){
        customClass.getImports().forEach(importClassPath -> {
            classString.append("import ").append(importClassPath).append(";");
        });
    }

    private void setClassName(){
        classString.append("public class ").append(customClass.getClassName()).append("{");
    }

    private void setCloseClass(){
        classString.append("}");
    }

}
