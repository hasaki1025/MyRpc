package com.myrpc.Factory;

import com.myrpc.context.RpcProperties;
import com.myrpc.net.ProviderServer;
import com.myrpc.net.RpcServiceChannelInitializer;
import com.myrpc.protocol.Enums.ChannelType;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProviderServerFactory {


    RpcProperties rpcProperties;
    ChannelType channelType;
    int port;

    EventLoopGroup group;
    EventLoopGroup childGroup;
    DefaultEventLoopGroup workerGroup;

    RpcServiceChannelInitializer rpcServiceChannelInitializer;


    public ProviderServerFactory( RpcProperties rpcProperties,
                                  @Qualifier("group") EventLoopGroup group,
                                  @Qualifier("childGroup")EventLoopGroup childGroup,
                                  @Qualifier("workerGroup") DefaultEventLoopGroup workerGroup,
                                  RpcServiceChannelInitializer rpcServiceChannelInitializer) throws Exception {
        this.rpcProperties=rpcProperties;
        this.channelType = rpcProperties.getRpcNetProperties().getChannelType();
        this.port = rpcProperties.getRpcNetProperties().getPort();
        this.group = group;
        this.childGroup = childGroup;
        this.workerGroup = workerGroup;
        this.rpcServiceChannelInitializer = rpcServiceChannelInitializer;
    }

    /**
     * @return
     * @throws Exception
     */
    @Bean("com.myrpc.net.ProviderServer")
    public ProviderServer providerServer() throws Exception {
        return new ProviderServer(group,childGroup,workerGroup,channelType,rpcServiceChannelInitializer,rpcProperties);
    }


}
