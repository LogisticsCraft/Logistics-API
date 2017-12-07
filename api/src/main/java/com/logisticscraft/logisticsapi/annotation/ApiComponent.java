package com.logisticscraft.logisticsapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.SOURCE)
@Target(value = {ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ApiComponent {}
