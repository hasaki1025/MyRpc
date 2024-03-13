package com.myrpc.protocol;

import com.myrpc.Util.MessageUtil;
import com.myrpc.protocol.Enums.EncryptionMethod;
import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.protocol.Enums.SerializableType;
import com.myrpc.protocol.Enums.Status;
import com.myrpc.protocol.content.RequestContent;
import com.myrpc.protocol.content.ResponseContent;
import lombok.Getter;
import lombok.Setter;


import java.util.Map;

@Getter
@Setter
public class BinaryMessage  implements Message{

    Map<Integer,Integer> headers;
    byte[] content;
    int headerSize;
    int contentSize;
    MessageType messageType;
    boolean requiredResponse;
    int size;
    int seq;
    Status status;




    public BinaryMessage(RPCRequest request, byte[] content, Map<Integer,Integer> headers)
    {
        this.headers=headers;
        this.content=content;
        this.size= MessageUtil.countSize(headers,content);
        this.headerSize=MessageUtil.countHeaderSize(headers);
        this.contentSize=content.length;
        this.messageType=request.getMessageType();
        this.requiredResponse=request.isRequiredResponse();
        this.seq=request.getSeq();
        this.status=request.getStatus();
    }

    public BinaryMessage(RPCResponse response,byte[] content,Map<Integer,Integer> headers)
    {
        this.headers=headers;
        this.content=content;
        this.size= MessageUtil.countSize(headers,content);
        this.headerSize=MessageUtil.countHeaderSize(headers);
        this.contentSize=content.length;
        this.messageType=response.getMessageType();
        this.requiredResponse=response.isRequiredResponse();
        this.seq=response.getSeq();
        this.status=response.getStatus();
    }


    public BinaryMessage(Map<Integer, Integer> headers, byte[] content, MessageType messageType, boolean requiredResponse, int size, int seq, Status status) {
        this.headers = headers;
        this.content = content;
        this.messageType = messageType;
        this.requiredResponse = requiredResponse;
        this.size = size;
        this.seq = seq;
        this.status = status;
        this.headerSize=MessageUtil.countHeaderSize(headers);
        this.contentSize=content.length;
    }

    /**
     * @return 返回二进制流的报文内容
     */
    @Override
    public byte[] getContent() {
        return content;
    }

    public RPCRequest toRPCRequest(RequestContent content) {
        SerializableType serializableType = SerializableType.forInt(HeaderMap.getHeaderValue(headers, SerializableType.class.getCanonicalName()));
        EncryptionMethod encryptionMethod = EncryptionMethod.forInteger(HeaderMap.getHeaderValue(headers, EncryptionMethod.class.getCanonicalName()));
        return new RPCRequest(content, serializableType, encryptionMethod, this.requiredResponse, this.seq);
    }

    public RPCResponse toRPCResponse(ResponseContent content) {
        SerializableType serializableType = SerializableType.forInt(HeaderMap.getHeaderValue(headers, SerializableType.class.getCanonicalName()));
        EncryptionMethod encryptionMethod = EncryptionMethod.forInteger(HeaderMap.getHeaderValue(headers, EncryptionMethod.class.getCanonicalName()));
        return new RPCResponse(content, serializableType, encryptionMethod, this.seq,this.status);
    }
}
