package com.myrpc.net;

import com.myrpc.protocol.content.Content;
import com.myrpc.protocol.content.RequestContent;
import com.myrpc.protocol.content.ResponseContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONConvertor implements SerializableConvertor{





    final ObjectMapper objectMapper=new ObjectMapper();


    /**
     * @param content
     * @return
     */

    public Content deserialize(byte[] content,boolean isRequestContent) throws Exception {
        Class<? extends Content> clazz = isRequestContent ? RequestContent.class : ResponseContent.class;
        Content object = objectMapper.readValue(content, clazz);
        if (object == null)
            throw new Exception("JSON Convertor Exception");
        return  object;
    }



    /**
     * @param content
     * @return
     */
    @Override
    public byte[] serialize(Content content) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(content);
    }





}
