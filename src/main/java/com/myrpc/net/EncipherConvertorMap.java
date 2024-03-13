package com.myrpc.net;

import com.myrpc.context.RpcProperties;
import com.myrpc.protocol.Enums.EncryptionMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class EncipherConvertorMap {

    public final ConcurrentHashMap<EncryptionMethod,EncipherConvertor> map=new ConcurrentHashMap<>();

    RpcProperties rpcProperties;

    public EncipherConvertorMap(RpcProperties rpcProperties) throws Exception {
        this.rpcProperties = rpcProperties;
        map.put(EncryptionMethod.DEFAULT,new DefaultConvertor());
        map.put(EncryptionMethod.AES,new AESConvertor(rpcProperties.getRpcNetProperties().getProtocolProperties().getAES_SecretKey()));
        map.put(EncryptionMethod.RSA,new RSAConvertor());
    }

    public byte[] encrypt(byte[] data,EncryptionMethod encryptionMethod) throws Exception {
        EncipherConvertor convertor = map.get(encryptionMethod);
        return convertor.encrypt(data);
    }

    public byte[] decrypt(byte[] data,EncryptionMethod encryptionMethod) throws Exception {
        EncipherConvertor convertor = map.get(encryptionMethod);
        return convertor.decrypt(data);
    }

}
