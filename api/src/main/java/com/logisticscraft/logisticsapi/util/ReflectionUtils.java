package com.logisticscraft.logisticsapi.util;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Consumer;

@UtilityClass
public class ReflectionUtils {

    private MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private MethodType
            RUNNABLE_TYPE = MethodType.methodType(Runnable.class),
            CONSUMER_TYPE = MethodType.methodType(Consumer.class),
            VOID_TYPE = MethodType.methodType(void.class),
            VOID_OBJECT_CLASS = MethodType.methodType(void.class, Object.class),
            OBJECT_TYPE = MethodType.methodType(Object.class);

    private String RUN_METHOD_NAME = "run", ACCEPT_METHOD_NAME = "run";

    private Cache<Method, Runnable> STATIC_METHOD_RUNNABLE_CACHE = CacheBuilder.newBuilder()
            .weakValues()
            .build();

    public static <T extends Annotation> T getClassAnnotation(@NonNull Object object, @NonNull Class<T> annotation) {
        Class<?> clazz = object.getClass();
        T result = clazz.getAnnotation(annotation);
        if (result == null) {
            throw new IllegalStateException("The class '" + clazz.getName() + "' does not have the @"
                    + annotation.getSimpleName() + " Annotation!");
        }
        return result;
    }

    public static Collection<Field> getFieldsRecursively(@NonNull Class<?> startClass, @NonNull Class<?> exclusiveParent) {
        Collection<Field> fields = Lists.newArrayList(startClass.getDeclaredFields());
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null && !(parentClass.equals(exclusiveParent))) {
            fields.addAll(getFieldsRecursively(parentClass, exclusiveParent));
        }

        return fields;
    }

    public static Collection<Method> getMethodsRecursively(@NonNull Class<?> startClass, @NonNull Class<?> exclusiveParent) {
        Collection<Method> methods = Lists.newArrayList(startClass.getDeclaredMethods());
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null && !(parentClass.equals(exclusiveParent))) {
            methods.addAll(getMethodsRecursively(parentClass, exclusiveParent));
        }

        return methods;
    }

    @SneakyThrows
    public Runnable toRunnable(@NonNull final Method method) {
        return STATIC_METHOD_RUNNABLE_CACHE.get(method, () -> {
            Preconditions.checkArgument(method.getParameterCount() == 0, "Method should have no parameters");

            final MethodHandle methodHandle;
            try {
                methodHandle = LOOKUP.unreflect(method);
            } catch (final IllegalAccessException e) {
                throw new RuntimeException("Unable to unreflect a method", e);
            }

            try {
                return (Runnable) LambdaMetafactory.metafactory(
                        LOOKUP, RUN_METHOD_NAME, RUNNABLE_TYPE, VOID_TYPE, methodHandle, VOID_TYPE
                ).getTarget().invoke();
            } catch (final Throwable throwable) {
                throw new RuntimeException("An exception occurred while creating a Runnable from method", throwable);
            }
        });
    }

    public Runnable toRunnable(@NonNull final Method method, @NonNull final Object instance) {
        Preconditions.checkArgument(method.getParameterCount() == 0, "Method should have no parameters");

        final MethodHandle methodHandle;
        try {
            methodHandle = LOOKUP.unreflect(method);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("Unable to unreflect a method", e);
        }

        try {
            return (Runnable) LambdaMetafactory.metafactory(
                    LOOKUP, RUN_METHOD_NAME, MethodType.methodType(Runnable.class, instance.getClass()),
                    VOID_TYPE, methodHandle, VOID_TYPE
            ).getTarget().invokeExact(instance);
        } catch (final Throwable throwable) {
            throw new RuntimeException("An exception occurred while creating a Runnable from method", throwable);
        }
    }

    public <T> Consumer<T> toInstanceConsumer(@NonNull final Method method,
                                              @NonNull final Class<? extends T> instanceType) {
        Preconditions.checkArgument(method.getParameterCount() == 0, "Method should have no parameters");

        final MethodHandle methodHandle;
        try {
            methodHandle = LOOKUP.unreflect(method);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("Unable to unreflect a method", e);
        }

        try {
            //noinspection unchecked
            return (Consumer<T>) LambdaMetafactory.metafactory(
                    LOOKUP, ACCEPT_METHOD_NAME, CONSUMER_TYPE,
                    VOID_OBJECT_CLASS, methodHandle, MethodType.methodType(void.class, instanceType)
            ).getTarget().invokeExact();
        } catch (final Throwable throwable) {
            throw new RuntimeException("An exception occurred while creating a Runnable from method", throwable);
        }
    }
}
