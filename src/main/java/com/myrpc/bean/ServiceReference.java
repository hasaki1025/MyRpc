package com.myrpc.bean;

import com.myrpc.Factory.ProxyFactory;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
@Data
public class ServiceReference<T> implements FactoryBean<T> {


    private Class<?> interfaceClass;


    private T proxy=null;
    ProxyFactory proxyFactory;

    private final String serviceName;

    public ServiceReference(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
        this.serviceName=interfaceClass.getCanonicalName();
    }

    public ServiceReference(Class<?> interfaceClass, String serviceName) {
        this.interfaceClass = interfaceClass;
        this.serviceName = serviceName;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public T getObject() throws Exception {
        if (proxy==null)
        {
            proxy= (T) proxyFactory.getServiceProxyInstance(interfaceClass);
        }

        return proxy;
    }

    /**
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    /**
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
