package com.myrpc.Factory;

import com.myrpc.context.ProtocolProperties;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.net.RPCServiceInstance;
import com.myrpc.protocol.RPCRequest;
import com.myrpc.protocol.content.RequestContent;

import java.lang.reflect.Method;

public class RpcRequestFactory {


    public static RPCRequest createRequest(Class<?> interfaceClass, Method method, Object[] args,RpcServiceContext context) throws Exception {
        RPCServiceInstance instance = context.getServiceInstanceByInterfaceClass(interfaceClass);
        ProtocolProperties properties = context.getRpcProperties().getRpcNetProperties().getProtocolProperties();
        boolean requiredResponse = !method.getReturnType().equals(void.class);
        RequestContent content = createRequestContent(method, args, instance);
        return new RPCRequest(content, properties.getSerializableType(), properties.getEncryptionMethod(), requiredResponse);
    }

    public static RequestContent createRequestContent(Method method,Object[] args,RPCServiceInstance instance)
    {
        RequestContent content = new RequestContent();
        content.setMethodName(method.getName());
        content.setArgs(args);
        content.setServiceName(instance.getServiceName());
        return content;
    }



}
