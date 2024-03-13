package com.myrpc.protocol;

import com.myrpc.protocol.Enums.EncryptionMethod;
import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.protocol.Enums.SerializableType;
import com.myrpc.protocol.Enums.Status;
import com.myrpc.protocol.content.ResponseContent;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
@Getter
@Setter
public class RPCResponse extends AbstractMessage implements Serializable {


    ResponseContent content;


    SerializableType serializableType;
    EncryptionMethod encryptionMethod;



    MessageType messageType=MessageType.response;
    boolean requiredResponse=false;
    int size;
    int seq;

    Status status;

    public RPCResponse(ResponseContent content, SerializableType serializableType, EncryptionMethod encryptionMethod, int seq, Status status) {
        this.content = content;
        this.serializableType = serializableType;
        this.encryptionMethod = encryptionMethod;
        this.seq = seq;
        this.status = status;
    }

    public RPCResponse(Map<Integer, Integer> headerMap, MessageType messageType, boolean requiredResponse, int size, int seq, Status status, ResponseContent content) {
        super(headerMap, messageType, requiredResponse, size, seq, status);

        int serializableTypeIndex = HeaderMap.getHeaderIndex(SerializableType.class.getCanonicalName());
        int serializableTypeValue = headerMap.get(serializableTypeIndex);
        serializableType=SerializableType.forInt(serializableTypeValue);
        int encryptionMethodIndex = HeaderMap.getHeaderIndex(EncryptionMethod.class.getCanonicalName());
        int encryptionMethodValue = headerMap.get(encryptionMethodIndex);
        encryptionMethod=EncryptionMethod.forInteger(encryptionMethodValue);
        this.content=content;
    }


    public BinaryMessage toBinaryMessage(byte[] bytes)
    {
        return new BinaryMessage(messageType, isRequiredResponse(), getHeaderMap(), getSeq(), getStatus(), bytes);
    }
}
