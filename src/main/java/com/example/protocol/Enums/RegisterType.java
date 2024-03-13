package com.example.protocol.Enums;

import com.example.net.client.NacosClient;
import com.example.net.client.RegisterClient;
import io.netty.channel.Channel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
