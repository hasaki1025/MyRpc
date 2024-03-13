package com.myrpc.protocol.Enums;

import com.myrpc.net.client.NacosClient;
import com.myrpc.net.client.RegisterClient;

public enum RegisterType {


    NACOS(0);



    private static final Class<? extends RegisterClient>[] registerClass=new Class[]{
            NacosClient.class
    };


    public static Class<? extends RegisterClient> ToRegisterClass(RegisterType registerType)
    {
        return registerClass[registerType.value];
    }
    final int value;
    RegisterType(int i) {
        this.value=i;
    }




}
