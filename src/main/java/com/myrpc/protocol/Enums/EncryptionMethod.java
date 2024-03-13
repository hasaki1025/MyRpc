package com.myrpc.protocol.Enums;

import com.myrpc.net.AESConvertor;
import com.myrpc.net.DefaultConvertor;
import com.myrpc.net.EncipherConvertor;
import com.myrpc.net.RSAConvertor;

public enum EncryptionMethod {
    DEFAULT(0),RSA(1),AES(2);

    public static final int headerID=2;
    private final static EncryptionMethod[] hash={DEFAULT,RSA,AES};

    private final static Class<? extends EncipherConvertor>[] encryptionMethodClass=new Class[]{
      DefaultConvertor.class, RSAConvertor.class, AESConvertor.class
    };

    public static Class<? extends EncipherConvertor> toEncryptionMethodClass(EncryptionMethod method)
    {
        return encryptionMethodClass[method.value];
    }


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
