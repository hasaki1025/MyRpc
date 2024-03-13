package com.example.net;

import io.netty.channel.ChannelHandler;

import java.util.List;

public class RpcProviderServerChannelInitializer extends RpcServerChannelInitializer {

    List<ChannelHandler> channelHandlers;

    /**
     * 初始化信道
     * @param handlersChain handler处理器链
     */
    public RpcProviderServerChannelInitializer(List<ChannelHandler> handlersChain) {
        super(handlersChain);
        this.channelHandlers = handlersChain;
    }
}
