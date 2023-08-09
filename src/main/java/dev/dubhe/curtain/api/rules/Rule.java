package dev.dubhe.curtain.api.rules;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rule {
    String[] categories();

    Class<? extends IValidator<?>>[] validators() default {};

    String[] suggestions() default {};

    String serializedName() default "";
}
