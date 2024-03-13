package com.example.Annotation;

import java.lang.annotation.*;

/**
 * 对于不参与远程调用的方法进行手动标注
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NonRemoteMethod {
}
