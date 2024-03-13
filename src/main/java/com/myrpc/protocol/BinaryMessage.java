package com.myrpc.protocol;

import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.protocol.Enums.Status;
import com.myrpc.protocol.content.RequestContent;
import com.myrpc.protocol.content.ResponseContent;
import lombok.Getter;
import lombok.Setter;


import java.util.Map;

@Getter
@Setter
public class BinaryMessage extends AbstractMessage {

    Map<Integer,Integer> headerMap;
    byte[] content;
    int headerSize;
    int contentSize;




    public BinaryMessage(MessageType messageType, boolean requiredResponse, Map<Integer, Integer> headerMap, int seq, Status status, byte[] content) {
        super(headerMap,messageType,requiredResponse,headerMap.size()*2+1+ content.length,seq,status);
        this.headerSize = headerMap.size() * 2 + 1;
        this.contentSize = size - headerSize;
    }

    public RPCRequest toRPCRequest(RequestContent content)
    {
        return new RPCRequest(
                headerMap, messageType, requiredResponse, size, seq, status,content
        );
    }
    public RPCResponse toRPCResponse(ResponseContent content)
    {
        return new RPCResponse(
                headerMap,messageType,requiredResponse,size,seq,status,content
        );
    }


    /**
     * @return 返回二进制流的报文内容
     */
    @Override
    public byte[] getContent() {
        return content;
    }
}
