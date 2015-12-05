package com.epam.core.annotations;


import com.epam.core.datafactory.RandomType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Data {

    RandomType type();

    int min() default 1;

    int max() default 7;

    String join() default "";
}
