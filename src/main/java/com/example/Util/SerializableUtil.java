package com.example.Util;

import com.example.net.JAVAObjectConvertor;
import com.example.net.JSONConvertor;
import com.example.net.ProtoBufferConvertor;
import com.example.net.SerializableConvertor;
import com.example.protocol.BinaryMessage;
import com.example.protocol.Enums.SerializableType;
import com.example.protocol.HeaderMap;
import com.example.protocol.RPCRequest;
import com.example.protocol.RPCResponse;
import com.example.protocol.content.Content;

import java.util.HashMap;
import java.util.Map;

public class SerializableUtil {

    //TODO 暂未初始化
    private final HashMap<SerializableType, SerializableConvertor> hashMap=new HashMap<>();

    public SerializableUtil() {
        hashMap.put(SerializableType.JSON, new JSONConvertor());
        hashMap.put(SerializableType.JAVA,new JAVAObjectConvertor());
        hashMap.put(SerializableType.PROTO_BUFFER,new ProtoBufferConvertor());
    }

    public SerializableType getSerializableType(Map<Integer,Integer> headers)
    {
        int index = HeaderMap.getHeaderIndex(SerializableType.class.getCanonicalName());
        return SerializableType.forInt(headers.get(index));
    }

    public Content deserialize(SerializableType serializableType, byte[] content,boolean isRequest) throws Exception {
        return hashMap.get(serializableType).deserialize(content,isRequest);
    }



    public byte[] serialize(SerializableType serializableType, Content object) throws Exception {
        return hashMap.get(serializableType).serialize(object);
    }
}
