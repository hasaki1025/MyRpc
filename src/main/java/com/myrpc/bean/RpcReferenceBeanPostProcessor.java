package com.myrpc.bean;

import com.myrpc.Annotation.RpcController;
import com.myrpc.Annotation.RpcReference;
import com.myrpc.Factory.ProxyFactory;
import com.myrpc.Util.RpcReferenceUtil;
import com.myrpc.context.RpcServiceContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
@Component
public class RpcReferenceBeanPostProcessor implements BeanPostProcessor {

    final ProxyFactory proxyFactory;

    final RpcServiceContext context;






    public RpcReferenceBeanPostProcessor(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
        this.context = proxyFactory.getRequestFactory().getContext();
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
                        //将标注有RpcReference注解的变量注入代理对象
                        field.set(bean,proxyFactory.getServiceProxyInstance(fieldClass));
                        context.addRemoteService(fieldClass, RpcReferenceUtil.getServiceName(field.getAnnotation(RpcReference.class),fieldClass));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
