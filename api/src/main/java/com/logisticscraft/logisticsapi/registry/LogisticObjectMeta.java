package com.logisticscraft.logisticsapi.registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.*;

/**
 * @author JARvis (Пётр) PROgrammer
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LogisticObjectMeta {
    @Nonnull
    LogisticObjectType type();

    @Nonnull
    String displayName() default "";

    @Nonnull
    String localizedName() default "";

    @Nullable
    String[] tags() default {};

    @Nullable
    String description() default "";

    @Nullable
    String descriptionLocalized() default "";
}
