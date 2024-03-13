package com.myrpc.Factory;

import com.myrpc.context.ProtocolProperties;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.net.RPCServiceInstance;
import com.myrpc.protocol.RPCRequest;
import com.myrpc.protocol.content.RequestContent;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
@Component
@Getter
public class RpcRequestFactory {


    RpcServiceContext context;

    public RpcRequestFactory(RpcServiceContext context) {
        this.context = context;
    }


    public  RPCRequest createRequest(RPCServiceInstance instance, Method method, Object[] args,int seq) throws Exception {
        //RPCServiceInstance instance = context.getServiceInstanceByInterfaceClass(interfaceClass);
        ProtocolProperties properties = context.getRpcProperties().getRpcNetProperties().getProtocolProperties();
        boolean requiredResponse = !method.getReturnType().equals(void.class);
        RequestContent content = createRequestContent(method, args, instance);
        return new RPCRequest(content, properties.getSerializableType(), properties.getEncryptionMethod(), requiredResponse,seq);
    }

    public  RequestContent createRequestContent(Method method,Object[] args,RPCServiceInstance instance)
    {
        RequestContent content = new RequestContent();
        content.setMethodName(method.getName());
        content.setArgs(args);
        content.setServiceName(instance.getServiceName());
        return content;
    }



}
