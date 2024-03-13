package com.myrpc.net;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrpc.protocol.content.ResponseContent;

import java.io.IOException;

public class ResponseContentJsonDeserializer extends JsonDeserializer<ResponseContent> {


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
    public ResponseContent deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JacksonException {
        ResponseContent content = new ResponseContent();
        TreeNode root = jsonParser.getCodec().readTree(jsonParser);
        TreeNode isSuccessfulNode = root.get("successful");
        content.setSuccessful(objectMapper.readValue(isSuccessfulNode.traverse(),Boolean.class));
        TreeNode resultClassNode = root.get("resultClass");
        String resultClass = objectMapper.readValue(resultClassNode.traverse(), String.class);
        content.setResultClass(resultClass);
        Class<?> clazz = null;
        try {
            clazz = Class.forName(resultClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        TreeNode resultNode = root.get("result");
        content.setResult(objectMapper.readValue(resultNode.traverse(),clazz));
        return content;
    }
}
