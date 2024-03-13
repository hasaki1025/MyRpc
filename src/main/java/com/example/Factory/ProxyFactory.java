package com.example.Factory;

import com.example.net.client.ServiceClient;
import com.example.protocol.content.ResponseContent;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProxyFactory {




    ServiceClient client;

    RpcRequestFactory requestFactory;

    public ProxyFactory(ServiceClient client, RpcRequestFactory requestFactory) {
        this.client = client;
        this.requestFactory = requestFactory;
    }

    public Object getServiceProxyInstance(Class<?> serviceClass){
        ServiceProxyInvocationHandler handler = new ServiceProxyInvocationHandler(client, requestFactory);
        return Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, handler);
    }



    public static Object getReturnValueProxy(Class<?> clazz, Future<ResponseContent> future) throws ExecutionException, InterruptedException {
        if (clazz.isPrimitive())
            return future.get();
        ReturnValueMethodInterceptor returnValueProxyFactory = new ReturnValueMethodInterceptor(future);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(returnValueProxyFactory);
        return enhancer.create();
    }


}
