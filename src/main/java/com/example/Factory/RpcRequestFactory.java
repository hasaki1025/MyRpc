package com.example.Factory;

import com.alibaba.nacos.api.remote.request.Request;
import com.example.Annotation.RpcService;
import com.example.context.ProtocolProperties;
import com.example.context.RpcServiceContext;
import com.example.net.RPCServiceInstance;
import com.example.net.client.RegisterClient;
import com.example.protocol.Enums.MessageType;
import com.example.protocol.RPCRequest;
import com.example.protocol.content.RequestContent;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class RpcRequestFactory {




    RegisterClient registerClient;

    RpcServiceContext context;

    public RpcRequestFactory(RegisterClient registerClient, RpcServiceContext context) {
        this.registerClient = registerClient;
        this.context = context;
    }

    public RPCRequest createRequest(Class<?> interfaceClass, Method method, Object[] args, int seq) throws Exception {

        RPCServiceInstance instance = context.getServiceInstanceByInterfaceClass(interfaceClass);
        ProtocolProperties properties = context.getRpcProperties().getRpcNetProperties().getProtocolProperties();
        boolean requiredResponse = !method.getReturnType().equals(void.class);
        RequestContent content = createRequestContent(method, args, instance);
        return new RPCRequest(content, properties.getSerializableType(), properties.getEncryptionMethod(), seq, requiredResponse);
    }

    public RequestContent createRequestContent(Method method,Object[] args,RPCServiceInstance instance)
    {
        RequestContent content = new RequestContent();
        content.setMethodName(method.getName());
        content.setArgs(args);
        content.setServiceName(instance.getServiceName());
        return content;
    }



}
