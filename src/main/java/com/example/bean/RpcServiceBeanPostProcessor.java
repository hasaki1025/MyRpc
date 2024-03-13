package com.example.bean;

import com.example.Annotation.RpcService;
import com.example.Factory.ServiceInstanceFactory;
import com.example.Util.BeanUtil;
import com.example.context.RpcServiceContext;
import com.example.net.RPCServiceInstance;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class RpcServiceBeanPostProcessor implements BeanPostProcessor {


    RpcServiceContext serviceContext;

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
        if (rpcServiceAnnotation ==null)
            return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        try {
            RPCServiceInstance instance = ServiceInstanceFactory.getServiceInstance(serviceContext.getRpcProperties(), rpcServiceAnnotation);
            serviceContext.addLocalService(instance,bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }


}
