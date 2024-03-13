package com.myrpc.Factory;

import com.myrpc.context.ProtocolProperties;
import com.myrpc.context.RpcProperties;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.protocol.Enums.Status;
import com.myrpc.protocol.RPCResponse;
import com.myrpc.protocol.content.ResponseContent;
import org.springframework.stereotype.Component;

@Component
public class RpcResponseFactory {


    RpcProperties rpcProperties;

    public RpcResponseFactory(RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
    }

    public  RPCResponse createResponse(ResponseContent content, int seq) throws Exception {
        ProtocolProperties properties = rpcProperties.getRpcNetProperties().getProtocolProperties();
        Status status=content.isSuccessful() ? Status.OK : Status.Error;
        return new RPCResponse(content, properties.getSerializableType(), properties.getEncryptionMethod(), seq,status);
    }
}
