package com.myrpc.Factory;

import com.myrpc.context.RpcServiceContext;
import com.myrpc.protocol.content.ResponseContent;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
@Component
public class ProxyFactory {



    RpcServiceContext context;

    public ProxyFactory(RpcServiceContext context) {
        this.context = context;
    }

    private final ConcurrentHashMap<Class<?>,Object> proxyCache=new ConcurrentHashMap<>();


    /**
     * @param serviceClass 被代理的接口的Class
     * @return 代理对象
     */
    public Object getServiceProxyInstance(Class<?> serviceClass){
        if (proxyCache.containsKey(serviceClass))
            return proxyCache.get(serviceClass);
        ServiceProxyInvocationHandler handler = new ServiceProxyInvocationHandler(context ,serviceClass);
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
