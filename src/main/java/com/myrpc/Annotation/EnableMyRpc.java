package com.myrpc.Annotation;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ComponentScan(basePackages = {"com.myrpc"})
//@Import({RpcRegisterRunner.class, RpcConsumerRunner.class, RpcProviderRunner.class})
public @interface EnableMyRpc {
}
