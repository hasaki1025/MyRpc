package com.example.net;


import io.netty.channel.ChannelHandler;

import java.util.List;

public class ServiceProviderServer extends Server{


    int port;

    private volatile boolean isInit=false;



    List<ChannelHandler> channelHandlers;

    public ServiceProviderServer(int port, List<ChannelHandler> channelHandlers) {
        this.port = port;
        this.channelHandlers = channelHandlers;
    }

    /**
     * 初始化方法
     */
    public void init() {
        if (!isInit)
        {
            isInit=true;
            super.init(port,new RpcProviderServerChannelInitializer(channelHandlers));
        }

    }
}
