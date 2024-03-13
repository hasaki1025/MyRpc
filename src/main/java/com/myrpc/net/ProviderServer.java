package com.myrpc.net;

import com.myrpc.context.RpcProperties;
import com.myrpc.protocol.Enums.ChannelType;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;

import java.util.List;

public class ProviderServer extends Server {


    public ProviderServer(EventLoopGroup bossGroup, EventLoopGroup childGroup, DefaultEventLoopGroup workerGroup, ChannelType channelType, RpcServiceChannelInitializer rpcServiceChannelInitializer, RpcProperties rpcProperties) throws Exception {
        super(bossGroup, childGroup, workerGroup, channelType, rpcServiceChannelInitializer, rpcProperties);
    }
}
