package com.example.Util;

import com.example.net.EncipherConvertor;
import com.example.protocol.BinaryMessage;
import com.example.protocol.Enums.EncryptionMethod;

import java.util.HashMap;

public class EncryptionUtil    {


    //TODO 需要完成初始化
    private final HashMap<EncryptionMethod, EncipherConvertor> convertorHashMap=new HashMap<>();

    public byte[] encode(EncryptionMethod method,byte[] msg)
    {
        return convertorHashMap.get(method).encode(msg);
    }

    public byte[] decode(EncryptionMethod method,byte[] msg)
    {
        return convertorHashMap.get(method).decode(msg);
    }
}
