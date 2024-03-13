package com.myrpc.protocol.Enums;

import com.myrpc.net.JAVAObjectConvertor;
import com.myrpc.net.JSONConvertor;
import com.myrpc.net.ProtoBufferConvertor;
import com.myrpc.net.SerializableConvertor;

public enum SerializableType {

    JAVA(0),JSON(1),PROTO_BUFFER(2);


    public static final int headerID=1;
    private final int value;
    SerializableType(int i) {
        this.value=i;
    }

    public int getValue() {
        return value;
    }


    private final  static SerializableType[] serializableTypes={JAVA,JSON,PROTO_BUFFER};

    private final static Class<? extends SerializableConvertor>[] serializableTypeClass=new Class[]{
            JAVAObjectConvertor.class, JSONConvertor.class, ProtoBufferConvertor.class
    };

    public  static Class<? extends SerializableConvertor> toSerializableTypeClass(SerializableType serializableType)
    {
        return serializableTypeClass[serializableType.value];
    }



    public static SerializableType forInt(int i)
    {
        return serializableTypes[i];
    }
}
