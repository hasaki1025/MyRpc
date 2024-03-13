package com.example.Factory;

import com.example.context.ProtocolProperties;
import com.example.context.RpcServiceContext;
import com.example.protocol.Enums.Status;
import com.example.protocol.RPCResponse;
import com.example.protocol.content.ResponseContent;

public class RpcResponseFactory {




    public static RPCResponse createResponse(ResponseContent content,int seq,RpcServiceContext context) throws Exception {
        ProtocolProperties properties = context.getRpcProperties().getRpcNetProperties().getProtocolProperties();
        Status status=content.isSuccessful() ? Status.OK : Status.Error;
        return new RPCResponse(content, properties.getSerializableType(), properties.getEncryptionMethod(), seq, status);
    }
}
