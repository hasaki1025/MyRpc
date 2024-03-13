package com.myrpc.protocol;

import com.myrpc.protocol.Enums.EncryptionMethod;
import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.protocol.Enums.SerializableType;
import com.myrpc.protocol.Enums.Status;
import com.myrpc.protocol.content.RequestContent;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class RPCRequest extends AbstractMessage implements Serializable {



    RequestContent content;

    SerializableType serializableType;
    EncryptionMethod encryptionMethod;

    Map<Integer,Integer> headerMap;
    MessageType messageType=MessageType.request;
    boolean requiredResponse;
    int size;
    int seq;

    Status status=Status.NULL;


    /**
     * 该构造方法产生的Request并没有初始化size和headerMap以及seq,seq需要交给Client初始化
     * @param content
     * @param serializableType
     * @param encryptionMethod
     * @param requiredResponse
     */
    public RPCRequest(RequestContent content, SerializableType serializableType, EncryptionMethod encryptionMethod,boolean requiredResponse) {
        this.content = content;
        this.serializableType = serializableType;
        this.encryptionMethod = encryptionMethod;
        this.requiredResponse=requiredResponse;
    }

    public RPCRequest(Map<Integer, Integer> headerMap, MessageType messageType, boolean requiredResponse, int size, int seq, Status status, RequestContent content) {
        super(headerMap, messageType, requiredResponse, size, seq, status);
        int serializableTypeIndex = HeaderMap.getHeaderIndex(SerializableType.class.getCanonicalName());
        int serializableTypeValue = headerMap.get(serializableTypeIndex);
        serializableType=SerializableType.forInt(serializableTypeValue);
        int encryptionMethodIndex = HeaderMap.getHeaderIndex(EncryptionMethod.class.getCanonicalName());
        int encryptionMethodValue = headerMap.get(encryptionMethodIndex);
        encryptionMethod=EncryptionMethod.forInteger(encryptionMethodValue);
        this.content=content;
    }


    public  BinaryMessage toBinaryMessage(byte[] bytes,Map<Integer,Integer> headers)
    {
        return new BinaryMessage(messageType, isRequiredResponse(), headers,getSeq(), getStatus(), bytes);
    }
}
