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
public class RPCRequest  implements Message {

    RequestContent content;
    SerializableType serializableType;
    EncryptionMethod encryptionMethod;
    MessageType messageType=MessageType.request;
    boolean requiredResponse;
    int size;
    int seq;
    Status status=Status.NULL;


    /**
     *
     * @param content
     * @param serializableType
     * @param encryptionMethod
     * @param requiredResponse
     */
    public RPCRequest(RequestContent content, SerializableType serializableType, EncryptionMethod encryptionMethod,boolean requiredResponse,int seq) {
        this.content = content;
        this.serializableType = serializableType;
        this.encryptionMethod = encryptionMethod;
        this.requiredResponse=requiredResponse;
        this.seq=seq;
    }

    public  BinaryMessage toBinaryMessage(byte[] bytes,Map<Integer,Integer> headers)
    {
        return new BinaryMessage(this,bytes,headers);
    }public  BinaryMessage toBinaryMessage(byte[] bytes)
    {
        return new BinaryMessage(this,bytes,HeaderMap.getHeaderMapOfRequest(this));
    }


}
