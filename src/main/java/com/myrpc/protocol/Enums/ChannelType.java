package com.myrpc.protocol.Enums;

import io.netty.channel.Channel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public enum ChannelType {
    NIO(0),EPOLL(1);








    private static final Class<? extends ServerSocketChannel>[] serverChannelClass=new Class[]{
            NioServerSocketChannel.class, EpollServerSocketChannel.class
    } ;
    private static final Class<? extends Channel>[] ChannelClass=new Class[]{
            NioSocketChannel.class, EpollSocketChannel.class
    };

    public static Class<? extends ServerSocketChannel> ToServerChannelClass(ChannelType channelType)
    {
        return serverChannelClass[channelType.value];
    }




    public static Class<? extends Channel> ToChannelClass(ChannelType channelType)
    {
        return ChannelClass[channelType.value];
    }
    final int value;
    ChannelType(int i) {
        this.value=i;
    }


}