package com.myrpc.net;

import com.myrpc.protocol.content.Content;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JAVAObjectConvertor implements SerializableConvertor {





    /**
     * @param content
     * @return
     * @throws Exception
     */

    public Content deserialize(byte[] content,boolean isRequestContent) throws Exception {

        ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(content));
        Content object = (Content) stream.readObject();
        if (object==null)
            throw new Exception("Java serialize get Null value");
        return  object;
    }



    /**
     * @param content
     * @return
     * @throws Exception
     */
    @Override
    public byte[] serialize(Content content) throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteArrayOutputStream);
        stream.writeObject(content);
        stream.flush();
        return byteArrayOutputStream.toByteArray();
    }





}
