package com.example.net;

import com.example.protocol.RPCRequest;
import com.example.protocol.RPCResponse;
import com.example.protocol.content.Content;

//TODO 等待实现
public class ProtoBufferConvertor implements SerializableConvertor {


    /**
     * @param content
     * @param isRequestContent
     * @return
     * @throws Exception
     */
    @Override
    public Content deserialize(byte[] content, boolean isRequestContent) throws Exception {
        return null;
    }

    /**
     * @param content
     * @return
     * @throws Exception
     */
    @Override
    public byte[] serialize(Content content) throws Exception {
        return new byte[0];
    }
}
