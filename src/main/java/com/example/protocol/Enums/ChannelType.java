package com.example.protocol.Enums;

import io.netty.channel.Channel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.HashMap;
import java.util.Map;

public enum ChannelType {
    NIO(0),EPOLL(1);



    private static final Class<? extends Channel>[] ChannelClass=new Class[]{
            NioSocketChannel.class, EpollSocketChannel.class
    };


    public static Class<? extends Channel> ToChannelClass(ChannelType channelType)
    {
        return ChannelClass[channelType.value];
    }
    final int value;
    ChannelType(int i) {
        this.value=i;
    }


}