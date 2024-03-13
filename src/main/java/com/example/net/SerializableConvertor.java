package com.example.net;

import com.example.protocol.RPCRequest;
import com.example.protocol.RPCResponse;
import com.example.protocol.content.Content;
import com.example.protocol.content.RequestContent;

public interface SerializableConvertor {






    Content deserialize(byte[] content,boolean isRequestContent) throws Exception;


    byte[] serialize(Content content) throws Exception;
}
