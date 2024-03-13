package com.example.bean;

import com.example.Annotation.RpcReference;
import com.example.Annotation.RpcService;
import com.example.Factory.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


public class ProxyBeanPostProcessor implements BeanPostProcessor {

    ProxyFactory proxyFactory;

    public ProxyBeanPostProcessor(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }


    /**
     * @param bean     the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for(Field field : fields)
        {
            RpcReference annotation = field.getAnnotation(RpcReference.class);
            if (annotation!=null)
            {
                field.setAccessible(true);
                try {
                    //将标注有RpcReference注解的变量注入代理对象
                    field.set(bean,proxyFactory.getServiceProxyInstance(beanClass));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
