package com.example.Annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RpcReference {

    Class<?> interfaceClass() default void.class;

    String serviceName() default "";




}
