package com.myrpc.Factory;

import com.myrpc.context.ProtocolProperties;
import com.myrpc.context.RpcProperties;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.protocol.Enums.Status;
import com.myrpc.protocol.RPCRequest;
import com.myrpc.protocol.RPCResponse;
import com.myrpc.protocol.content.ResponseContent;
import org.springframework.stereotype.Component;


public class RpcResponseFactory {


    /**
     * 根据接受的Request的类型生成响应
     * @param content
     * @param request
     * @return
     * @throws Exception
     */
    public static RPCResponse createResponse(ResponseContent content, RPCRequest request) throws Exception {
        Status status=content.isSuccessful() ? Status.OK : Status.Error;
        return new RPCResponse(content, request.getSerializableType(), request.getEncryptionMethod(), request.getSeq(),status);
    }
}
