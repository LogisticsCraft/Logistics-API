package com.logisticscraft.logisticsapi.utils;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

@UtilityClass
public class ReflectionUtils {

    public <T extends Annotation> T getClassAnnotation(Object object, Class<T> annotation) {
        Class<?> clazz = object.getClass();
        T result = clazz.getAnnotation(annotation);
        if (result == null) {
            throw new IllegalStateException("The class '" + clazz.getName() + "' does not have the @" + annotation.getSimpleName() + " Annotation!");
        }
        return result;
    }

    public static Collection<Field> getFieldsRecursively(@NonNull Class<?> startClass, @NonNull Class<?> exclusiveParent) {
        Collection<Field> fields = Lists.newArrayList(startClass.getDeclaredFields());
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null && (exclusiveParent == null || !(parentClass.equals(exclusiveParent)))) {
            fields.addAll(getFieldsRecursively(parentClass, exclusiveParent));
        }

        return fields;
    }

}
