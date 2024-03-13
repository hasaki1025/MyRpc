package com.example.protocol;

import com.example.protocol.Enums.EncryptionMethod;
import com.example.protocol.Enums.SerializableType;

import java.util.HashMap;
import java.util.Map;

public class HeaderMap {



   public static final HashMap<String, Integer> headerMap=new HashMap<>();



   static {
       headerMap.put(SerializableType.class.getCanonicalName(),SerializableType.headerID);
       headerMap.put(EncryptionMethod.class.getCanonicalName(),EncryptionMethod.headerID);
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
