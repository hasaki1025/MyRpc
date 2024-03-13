package com.myrpc.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrpc.protocol.content.Content;
import com.myrpc.protocol.content.RequestContent;
import com.myrpc.protocol.content.ResponseContent;
import org.msgpack.jackson.dataformat.MessagePackFactory;

public class MessagePackerConvertor implements SerializableConvertor {


    private final ObjectMapper objectMapper=new ObjectMapper(new MessagePackFactory());



    /**
     * @param content
     * @param isRequestContent
     * @return
     * @throws Exception
     */
    @Override
    public Content deserialize(byte[] content, boolean isRequestContent) throws Exception {
        Class<? extends Content> clazz = isRequestContent ? RequestContent.class : ResponseContent.class;
        Content object = objectMapper.readValue(content, clazz);
        if (object == null)
            throw new Exception("JSON Convertor Exception");
        return  object;
    }

    /**
     * @param content
     * @return
     * @throws Exception
     */
    @Override
    public byte[] serialize(Content content) throws Exception {
        return objectMapper.writeValueAsBytes(content);
    }
}
