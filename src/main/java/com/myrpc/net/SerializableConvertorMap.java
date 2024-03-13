package com.myrpc.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myrpc.context.RpcProperties;
import com.myrpc.protocol.Enums.SerializableType;
import com.myrpc.protocol.RPCRequest;
import com.myrpc.protocol.content.Content;
import com.myrpc.protocol.content.RequestContent;
import com.myrpc.protocol.content.ResponseContent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
@Component
public class SerializableConvertorMap {


    private final ConcurrentHashMap<SerializableType,SerializableConvertor> map=new ConcurrentHashMap<>();


    public SerializableConvertorMap() {
        map.put(SerializableType.JAVA,new JAVAObjectConvertor());
        map.put(SerializableType.JSON,new JSONConvertor());
        map.put(SerializableType.PROTO_BUFFER,new ProtoBufferConvertor());
    }
    public RequestContent deserializeRequestContent(byte[] content,SerializableType serializableType) throws Exception {
        return (RequestContent) deserialize(content, true, serializableType);
    }


    public ResponseContent deserializeResponseContent(byte[] content,SerializableType serializableType) throws Exception {
        return (ResponseContent) deserialize(content, false, serializableType);
    }



     Content deserialize(byte[] content, boolean isRequestContent,SerializableType serializableType) throws Exception {
        SerializableConvertor convertor = map.get(serializableType);
        return convertor.deserialize(content, isRequestContent);
    }



    public byte[] serialize(Content content,SerializableType serializableType) throws Exception {
        return map.get(serializableType).serialize(content);
    }



}
