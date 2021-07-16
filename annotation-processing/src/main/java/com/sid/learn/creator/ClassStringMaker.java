package com.sid.learn.creator;

public class ClassStringMaker {

    private CustomClass customClass;
    private StringBuilder classString;

    public ClassStringMaker(CustomClass customClass){
        this.customClass = customClass;
        this.classString = new StringBuilder();
    }

    public String classAsString(){
        setPackage();
        setImports();
        setClassName();
        setClassExtends();
        setClassOpen();
        setCustomFields();
        setSetterAndGetter();
        setAbstractMethod();
        setClassClose();
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
        classString.append("public ").append(customClass.getClassType()).append(" ")
        .append(customClass.getClassName()).append(" ");
    }

    private void setClassExtends(){
        if(customClass.getCustomClassExtends() == null)
            return;

        classString.append("extends ").append(customClass.getCustomClassExtends().getClassName());

        if(customClass.getCustomClassExtends().getParameters() == null)
            return;

        classString.append("<");
        customClass.getCustomClassExtends().getParameters().forEach(param -> {
            classString.append(param).append(",");
        });
        classString.replace(classString.lastIndexOf(","), classString.length(), "");
        classString.append(">");
    }

    private void setClassOpen(){
        classString.append("{").append("\n");
    }

    private void setCustomFields(){
        if(customClass.getClassFields() == null)
            return;

        customClass.getClassFields().forEach(customField -> {

            if(customField.getCustomAnnotations() != null){
                customField.getCustomAnnotations().forEach(customAnnotation -> {
                    classString.append("\t").append("@").append(customAnnotation.getName());
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

            classString.append("\t").append(customField.getModifier()).append(" ")
                    .append(customField.getReturnType()).append(" ")
                    .append(customField.getName()).append(";");
            classString.append("\n");
        });
    }

    private void setSetterAndGetter(){
        if(null == customClass.getClassFields())
            return;

        customClass.getClassFields().forEach(customField -> {
            setSetter(customField);
            setGetter(customField);
        });
    }

    private void setSetter(CustomField customField){
        classString.append("\n\t");
        classString.append("public").append(" ")
                .append("void").append(" ")
                .append("set" + customField.getName()).append("(").append(customField.getReturnType()).append(" ").append(customField.getName()).append("){").append("\n")
                .append("\t\t").append("this.").append(customField.getName()).append("=").append(customField.getName()).append(";").append("\n")
                .append("\t").append("}").append("\n");
    }

    private void setGetter(CustomField customField){
        classString.append("\n\t");
        classString.append("public").append(" ")
                .append(customField.getReturnType()).append(" ")
                .append("get" + customField.getName()).append("(){").append("\n")
                .append("\t\t").append("return this.").append(customField.getName()).append(";").append("\n")
                .append("\t").append("}").append("\n");
    }

    private void setAbstractMethod(){
        if(customClass.getCustomMethods() ==null)
            return;

        customClass.getCustomMethods().forEach(customMethod -> {
            classString.append("\t").append(customMethod.getModifier()).append(" ")
                    .append(customMethod.getReturnType()).append(" ")
                    .append(customMethod.getName()).append("(");

            if(customMethod.getMethodParameters() !=null){
                customMethod.getMethodParameters().forEach(methodParameter -> {
                    classString.append(methodParameter.getType()).append(" ")
                            .append(methodParameter.getName()).append(",");
                });
                classString.replace(classString.lastIndexOf(","), classString.length(), "");
            }
            classString.append(");\n");
        });
    }

    private void setClassClose(){
        classString.append("}").append("\n");
    }

}
