package com.myrpc.bean;

import com.myrpc.Annotation.RpcService;
import com.myrpc.Factory.ServiceInstanceFactory;
import com.myrpc.Util.BeanUtil;
import com.myrpc.context.RpcServiceContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class RpcServiceBeanPostProcessor implements BeanPostProcessor {


    final RpcServiceContext serviceContext;

    final ServiceInstanceFactory serviceInstanceFactory;

    public RpcServiceBeanPostProcessor(RpcServiceContext serviceContext, ServiceInstanceFactory serviceInstanceFactory) {
        this.serviceContext = serviceContext;
        this.serviceInstanceFactory = serviceInstanceFactory;
    }

    /**将符合条件的Bean注入到serviceContext中
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> beanClass = bean.getClass();
        RpcService rpcServiceAnnotation = BeanUtil.getRpcServiceAnnotation(beanClass);
        if (rpcServiceAnnotation ==null || beanClass.getInterfaces().length==0)
            return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        try {
            serviceContext.addLocalService(serviceInstanceFactory.getServiceInstance(beanClass),bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }


}
