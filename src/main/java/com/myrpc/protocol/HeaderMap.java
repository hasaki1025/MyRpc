package com.myrpc.protocol;

import com.myrpc.protocol.Enums.EncryptionMethod;
import com.myrpc.protocol.Enums.SerializableType;

import java.util.HashMap;
import java.util.Map;

public class HeaderMap {



   public static final HashMap<String, Integer> headerMap=new HashMap<>();



   static {
       headerMap.put(SerializableType.class.getCanonicalName(),SerializableType.headerID);
       headerMap.put(EncryptionMethod.class.getCanonicalName(),EncryptionMethod.headerID);
   }



   public static Map<Integer,Integer> getHeaderMapOfRequest(RPCRequest request)
   {
       HashMap<Integer, Integer> map = new HashMap<>();
       SerializableType serializableType = request.getSerializableType();
       EncryptionMethod encryptionMethod = request.getEncryptionMethod();
       map.put(getHeaderIndex(SerializableType.class.getCanonicalName()),serializableType.getValue());
       map.put(getHeaderIndex(EncryptionMethod.class.getCanonicalName()),encryptionMethod.getValue());
       return map;
   }
   public static Map<Integer,Integer> getHeaderMapOfResponse(RPCResponse response)
   {
       HashMap<Integer, Integer> map = new HashMap<>();
       SerializableType serializableType = response.getSerializableType();
       EncryptionMethod encryptionMethod = response.getEncryptionMethod();
       map.put(getHeaderIndex(SerializableType.class.getCanonicalName()),serializableType.getValue());
       map.put(getHeaderIndex(EncryptionMethod.class.getCanonicalName()),encryptionMethod.getValue());
       return map;
   }


    public static int getHeaderIndex(String headerName)
    {
        return headerMap.get(headerName);
    }

    public static int getHeaderValue(Map<Integer,Integer> headers,String headerName)
    {
        return headers.get(headerMap.get(headerName));
    }




}
