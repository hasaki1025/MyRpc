package com.myrpc.Util;

import com.myrpc.Annotation.RpcService;

import java.lang.reflect.Method;

public class BeanUtil {


    //TODO 之后是否可以支持RpcService放在方法上
    public static RpcService getRpcServiceAnnotation(Class<?> beanClass) {
        RpcService annotation = beanClass.getAnnotation(RpcService.class);
        if (annotation!=null)
            return annotation;
        Method[] methods = beanClass.getMethods();
        for (Method method:methods)
        {
            annotation= method.getAnnotation(RpcService.class);
            if (annotation!=null)
                return annotation;
        }
        return annotation;
    }
}
