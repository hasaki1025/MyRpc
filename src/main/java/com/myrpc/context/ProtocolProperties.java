package com.myrpc.context;

import com.myrpc.protocol.Enums.EncryptionMethod;
import com.myrpc.protocol.Enums.SerializableType;
import com.myrpc.protocol.HeaderMap;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Data
public class ProtocolProperties{

     SerializableType serializableType;

     EncryptionMethod encryptionMethod;

     @Getter
     Map<Integer,Integer> headers=new HashMap<>();


     public void setSerializableType(SerializableType serializableType) {
          this.serializableType = serializableType;
          headers.put(HeaderMap.getHeaderIndex(SerializableType.class.getCanonicalName()),serializableType.getValue());
     }

     public void setEncryptionMethod(EncryptionMethod encryptionMethod) {
          this.encryptionMethod = encryptionMethod;
          headers.put(HeaderMap.getHeaderIndex(EncryptionMethod.class.getCanonicalName()),encryptionMethod.getValue());
     }
}