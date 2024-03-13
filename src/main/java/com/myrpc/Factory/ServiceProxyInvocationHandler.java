package com.myrpc.Factory;

import com.myrpc.Annotation.NonRemoteMethod;
import com.myrpc.Annotation.RpcService;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.net.client.ServiceClient;
import com.myrpc.protocol.RPCRequest;
import com.myrpc.protocol.content.ResponseContent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Future;


public class ServiceProxyInvocationHandler implements InvocationHandler {




    RpcServiceContext context;

    Class<?> interfaceClass;

    public ServiceProxyInvocationHandler(RpcServiceContext context, Class<?> interfaceClass) {
        this.context = context;
        this.interfaceClass = interfaceClass;
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

        //仅仅对于接口定义的方法并且没有使用NonRemoteMethod注解标注的方法进行代理
        if (method.getDeclaringClass().equals(interfaceClass) && !method.isAnnotationPresent(NonRemoteMethod.class))
        {
            Class<?> returnType = method.getReturnType();

            RPCRequest rpcRequest = RpcRequestFactory.createRequest(interfaceClass, method, args,context);
            ServiceClient client = context.getServiceClient(rpcRequest.getContent().getServiceName());
            Future<ResponseContent> future = client.call(rpcRequest);
            //对于返回类型是基本类型的方法无法采用异步调用
            if (returnType.isPrimitive())
                return future.get();
            return ProxyFactory.getReturnValueProxy(returnType, future);
        }
        return method.invoke(proxy,args);

    }


}
