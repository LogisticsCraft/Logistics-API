package com.logisticscraft.logisticsapi.rewrite.utils;

import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;

@UtilityClass
public class AnnotationUtils {

    public static <T extends Annotation> T getClassAnnotation(Object object, Class<T> annotation) {
        Class<?> clazz = object.getClass();
        T result = clazz.getAnnotation(annotation);
        if(result == null){
            throw new IllegalStateException("The class '" + clazz.getName() + "' does not have the @" + annotation.getSimpleName() + " Annotation!");
        }
        return result;
    }

}
