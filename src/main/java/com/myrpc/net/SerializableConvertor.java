package com.myrpc.net;

import com.myrpc.protocol.content.Content;

public interface SerializableConvertor {






    Content deserialize(byte[] content,boolean isRequestContent) throws Exception;


    byte[] serialize(Content content) throws Exception;
}
