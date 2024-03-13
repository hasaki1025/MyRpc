package com.example.Factory;

import com.example.net.client.ServiceClient;
import com.example.protocol.content.ResponseContent;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProxyFactory {




    ServiceClient client;

    RpcRequestFactory requestFactory;

    private final ConcurrentHashMap<Class<?>,Object> proxyCache=new ConcurrentHashMap<>();

    public ProxyFactory(ServiceClient client, RpcRequestFactory requestFactory) {
        this.client = client;
        this.requestFactory = requestFactory;
    }

    /**
     * @param serviceClass 被代理的接口的Class
     * @return 代理对象
     */
    public Object getServiceProxyInstance(Class<?> serviceClass){
        if (proxyCache.containsKey(serviceClass))
            return proxyCache.get(serviceClass);
        ServiceProxyInvocationHandler handler = new ServiceProxyInvocationHandler(client, requestFactory,serviceClass);
        Object proxy = Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, handler);
        proxyCache.put(serviceClass, proxy);
        return proxy;
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
