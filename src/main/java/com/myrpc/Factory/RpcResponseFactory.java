package com.myrpc.Factory;

import com.myrpc.context.ProtocolProperties;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.protocol.Enums.Status;
import com.myrpc.protocol.RPCResponse;
import com.myrpc.protocol.content.ResponseContent;

public class RpcResponseFactory {




    public static RPCResponse createResponse(ResponseContent content,int seq,RpcServiceContext context) throws Exception {
        ProtocolProperties properties = context.getRpcProperties().getRpcNetProperties().getProtocolProperties();
        Status status=content.isSuccessful() ? Status.OK : Status.Error;
        return new RPCResponse(content, properties.getSerializableType(), properties.getEncryptionMethod(), seq, status);
    }
}
