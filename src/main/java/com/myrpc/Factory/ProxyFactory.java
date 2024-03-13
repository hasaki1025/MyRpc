package com.myrpc.Factory;

import com.myrpc.context.RpcServiceContext;
import com.myrpc.net.client.ServiceClientPool;
import com.myrpc.protocol.content.ResponseContent;
import lombok.Getter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
@Component
@Getter
public class ProxyFactory {



   final RpcRequestFactory requestFactory;
   final ServiceClientPool serviceClientPool;

    public ProxyFactory(RpcRequestFactory requestFactory, ServiceClientPool serviceClientPool) {
        this.requestFactory = requestFactory;
        this.serviceClientPool = serviceClientPool;
    }

    private final ConcurrentHashMap<Class<?>,Object> proxyCache=new ConcurrentHashMap<>();


    /**
     * @param serviceClass 被代理的接口的Class
     * @return 代理对象
     */
    public Object getServiceProxyInstance(Class<?> serviceClass){
        if (proxyCache.containsKey(serviceClass))
            return proxyCache.get(serviceClass);
        ServiceProxyInvocationHandler handler = new ServiceProxyInvocationHandler(requestFactory ,serviceClass,serviceClientPool);
        Object proxy = Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, handler);
        proxyCache.put(serviceClass, proxy);
        return proxy;
    }



    public static Object getReturnValueProxy(Class<?> clazz, Future<ResponseContent> future) throws ExecutionException, InterruptedException {
        if (clazz.isPrimitive())
            return future.get().getResult();
        ReturnValueMethodInterceptor returnValueProxyFactory = new ReturnValueMethodInterceptor(future);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(returnValueProxyFactory);
        return enhancer.create();
    }


}
