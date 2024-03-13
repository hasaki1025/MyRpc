package com.example.protocol.Enums;

public enum SerializableType {
    JAVA(0),JSON(1),PROTO_BUFFER(2);
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
