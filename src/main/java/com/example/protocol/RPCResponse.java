package com.example.protocol;

import com.example.protocol.Enums.EncryptionMethod;
import com.example.protocol.Enums.MessageType;
import com.example.protocol.Enums.SerializableType;
import com.example.protocol.Enums.Status;
import com.example.protocol.content.ResponseContent;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
@Getter
@Setter
public class RPCResponse extends AbstractMessage implements Serializable {


    ResponseContent content;


    SerializableType serializableType;
    EncryptionMethod encryptionMethod;



    public RPCResponse(Map<Integer, Integer> headerMap, MessageType messageType, boolean requiredResponse, int size, int seq, Status status,ResponseContent content) {
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
