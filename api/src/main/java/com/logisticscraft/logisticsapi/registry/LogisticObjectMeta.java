package com.logisticscraft.logisticsapi.registry;

import lombok.NonNull;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LogisticObjectMeta {

    @NonNull
    LogisticObjectType type();

    @NonNull
    String displayName() default "";

    @NonNull
    String localizedName() default "";

    String[] tags() default {};

    String description() default "";

    String descriptionLocalized() default "";

}
