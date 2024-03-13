package com.myrpc.protocol;

import com.myrpc.protocol.Enums.EncryptionMethod;
import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.protocol.Enums.SerializableType;
import com.myrpc.protocol.Enums.Status;
import com.myrpc.protocol.content.RequestContent;
import com.myrpc.protocol.content.ResponseContent;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
@Getter
@Setter
public class RPCResponse  implements Message {


    ResponseContent content;
    SerializableType serializableType;
    EncryptionMethod encryptionMethod;
    MessageType messageType=MessageType.response;
    boolean requiredResponse=false;
    int size;
    int seq;
    Status status;


    /**
     *
     * @param content
     * @param serializableType
     * @param encryptionMethod
     */
    public RPCResponse(ResponseContent content, SerializableType serializableType, EncryptionMethod encryptionMethod,int seq,Status status) {
        this.content = content;
        this.serializableType = serializableType;
        this.encryptionMethod = encryptionMethod;
        this.seq=seq;
        this.status=status;
    }

    public  BinaryMessage toBinaryMessage(byte[] bytes,Map<Integer,Integer> headers)
    {
        return new BinaryMessage(this,bytes,headers);
    }


}
