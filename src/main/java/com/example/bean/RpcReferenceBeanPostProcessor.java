package com.example.bean;

import com.example.Annotation.RpcController;
import com.example.Annotation.RpcReference;
import com.example.Factory.ProxyFactory;
import com.example.context.RpcServiceContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;


public class RpcReferenceBeanPostProcessor implements BeanPostProcessor {

    ProxyFactory proxyFactory;

    RpcServiceContext context;



    public RpcReferenceBeanPostProcessor(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }


    /**
     * @param bean     the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ServiceReference<?> reference)
        {
            reference.setProxyFactory(proxyFactory);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
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
        if (beanClass.isAnnotationPresent(RpcController.class))
        {
            for(Field field : fields)
            {
                field.setAccessible(true);
                try {
                    if (field.isAnnotationPresent(RpcReference.class) && field.get(bean)==null)
                    {

                        Class<?> fieldClass = field.getType();
                        String serviceName = field.getAnnotation(RpcReference.class).serviceName();
                        //将标注有RpcReference注解的变量注入代理对象
                        field.set(bean,proxyFactory.getServiceProxyInstance(fieldClass));
                        context.addRemoteService(fieldClass,serviceName);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
