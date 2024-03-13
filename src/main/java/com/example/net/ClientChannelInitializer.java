package com.example.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ClientChannelInitializer extends ChannelInitializer<Channel> {


    /**
     * 注入的处理器链
     */
    List<ChannelHandler> handlersChain;



    public ClientChannelInitializer(List<ChannelHandler> handlersChain) {
        this.handlersChain = handlersChain;
    }

    /**
     * 初始化信道
     * @param ch 信道
     */
    @Override
    protected void initChannel(Channel ch) {
        log.info("channel {}-->{} initialized",ch.localAddress(),ch.remoteAddress());
    }
}
