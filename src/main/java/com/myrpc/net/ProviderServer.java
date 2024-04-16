package com.myrpc.net;

import com.myrpc.protocol.Enums.ChannelType;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;

public class ProviderServer extends Server {





    public ProviderServer(EventLoopGroup bossGroup, EventLoopGroup childGroup, DefaultEventLoopGroup workerGroup, ChannelType channelType, RpcServiceChannelInitializer rpcServiceChannelInitializer, int port) {
        super(bossGroup, childGroup, workerGroup, channelType, rpcServiceChannelInitializer, port);
    }
}
