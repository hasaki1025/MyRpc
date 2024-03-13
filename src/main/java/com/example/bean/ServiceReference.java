package com.example.bean;

import com.example.Factory.ProxyFactory;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
@Data
public class ServiceReference<T> implements FactoryBean<T> {


    private Class<?> interfaceClass;


    private T proxy=null;
    ProxyFactory proxyFactory;

    public ServiceReference(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
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
