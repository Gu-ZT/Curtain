package dev.dubhe.curtain.api.rules;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rule {
    String[] categories();
    Class<? extends IValidator<?>>[] validators() default {};
    String[] suggestions();

    String serializedName() default "";
}
