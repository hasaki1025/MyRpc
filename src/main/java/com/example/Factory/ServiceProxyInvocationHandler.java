package com.example.Factory;

import com.example.net.client.ServiceClient;
import com.example.protocol.RPCRequest;
import com.example.protocol.content.ResponseContent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Future;


public class ServiceProxyInvocationHandler implements InvocationHandler {


    ServiceClient client;

    RpcRequestFactory requestFactory;


    public ServiceProxyInvocationHandler(ServiceClient client, RpcRequestFactory requestFactory) {
        this.client = client;
        this.requestFactory = requestFactory;
    }

    /**
     * 如何被代理的方法返回的是基本类型则只能采用同步调用，否则全部采用动态代理实现异步调用
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class<?> returnType = method.getReturnType();
        RPCRequest rpcRequest = requestFactory.getRpcRequest(proxy.getClass(), method, args);
        Future<ResponseContent> future = client.call(rpcRequest);
        //TODO 对于基础方法是否需要放行
        if (returnType.isPrimitive())
            return future.get();
        return ProxyFactory.getReturnValueProxy(returnType, future);
    }


}
