package com.example.protocol.Enums;

public enum EncryptionMethod {
    DEFAULT(0),RSA(1),AES(2);

    private final static EncryptionMethod[] hash={DEFAULT,RSA,AES};


    private final int value;
    EncryptionMethod(int i) {
        value=i;
    }


    public int getValue()
    {
        return value;
    }



    public static EncryptionMethod forInteger(int value)
    {
        return hash[value];
    }

}
