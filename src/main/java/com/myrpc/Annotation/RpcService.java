package com.myrpc.Annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 默认情况下标注为RPCService的类如果只实现了一个接口则该注解其他属性可以无需配置，如果实现了多个接口则必须指出RPCService的接口
 * 除此之外该注解的功能类似于@DubboService
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Component
public @interface RpcService {


    /**
     * @return 默认情况下为RPCService的接口名称
     */
    String serviceName() default "";

    String interfaceName() default "";

    Class<?> interfaceClass() default  void.class;


    String instanceId() default "";
    double weight() default 1.0D;
    boolean healthy() default true;
    boolean enabled() default true;
    boolean ephemeral() default true;
    String clusterName() default "";




}
