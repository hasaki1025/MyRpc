package com.myrpc.net;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrpc.protocol.content.RequestContent;

import java.io.IOException;

public class RequestContentJsonDeserializer extends JsonDeserializer<RequestContent> {


    ObjectMapper objectMapper=new ObjectMapper();
    /**
     * @param jsonParser    Parsed used for reading JSON content
     * @param context Context that can be used to access information about
     *             this deserialization activity.
     * @return
     * @throws IOException
     * @throws JacksonException
     */
    @Override
    public RequestContent deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JacksonException {
        RequestContent content = new RequestContent();
        TreeNode node = jsonParser.getCodec().readTree(jsonParser);
        content.setServiceName(objectMapper.readValue(node.get("serviceName").traverse(),String.class));;
        content.setMethodName(objectMapper.readValue(node.get("methodName").traverse(),String.class));
        TreeNode argClassNode = node.get("argClass");
        content.setArgClass(objectMapper.readValue(argClassNode.traverse(), String[].class));
        TreeNode argsNode = node.get("args");
        String[] argClass = content.getArgClass();
        int length = argClass.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++) {
            TreeNode arg = argsNode.get(i);
            Class<?> argC = null;
            try {
                argC = Class.forName(argClass[i]);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            objects[i] = objectMapper.readValue(arg.traverse(), argC);
        }
        content.setArgs(objects);
        return content;
    }
}
