package com.example.protocol.Enums;

import com.example.net.SerializableConvertor;

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



    public static SerializableType forInt(int i)
    {
        return serializableTypes[i];
    }
}
