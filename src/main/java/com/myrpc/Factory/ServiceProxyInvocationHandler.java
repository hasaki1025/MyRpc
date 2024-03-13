package com.myrpc.Factory;

import com.myrpc.Annotation.NonRemoteMethod;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.net.RPCServiceInstance;
import com.myrpc.net.client.ServiceClient;
import com.myrpc.net.client.ServiceClientPool;
import com.myrpc.protocol.RPCRequest;
import com.myrpc.protocol.content.ResponseContent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Future;


public class ServiceProxyInvocationHandler implements InvocationHandler {






    Class<?> interfaceClass;
    RpcRequestFactory requestFactory;

    final ServiceClientPool serviceClientPool;

    public ServiceProxyInvocationHandler(RpcRequestFactory requestFactory, Class<?> interfaceClass, ServiceClientPool serviceClientPool) {
        this.requestFactory=requestFactory;
        this.interfaceClass = interfaceClass;
        this.serviceClientPool = serviceClientPool;
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
            RpcServiceContext context = requestFactory.getContext();
            RPCServiceInstance instance = context.getServiceInstanceByInterfaceClass(interfaceClass);
            if (instance==null)
                throw new Exception("no such service Instance");
            ServiceClient client = serviceClientPool.getConnection(instance.getIp() + ":" + instance.getPort());
            RPCRequest rpcRequest = requestFactory.createRequest(instance, method, args,client.getNextSeq());
            Future<ResponseContent> future = client.call(rpcRequest);
            //对于返回类型是基本类型的方法无法采用异步调用
            if (returnType.isPrimitive())
                return future.get().getResult();
            return ProxyFactory.getReturnValueProxy(returnType, future);
        }
        return method.invoke(proxy,args);

    }


}
