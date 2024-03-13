package com.myrpc.Factory;

import com.myrpc.context.RpcProperties;
import com.myrpc.protocol.Enums.ChannelType;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventLoopFactory {


    ChannelType channelType;


    public EventLoopFactory(RpcProperties rpcProperties) throws Exception {
        channelType=rpcProperties.getRpcNetProperties().getChannelType();
    }

    @Bean
    EventLoopGroup group()
    {
        if (channelType.equals(ChannelType.NIO))
        {
            return new NioEventLoopGroup();
        } else if (channelType.equals(ChannelType.EPOLL)) {
            return new EpollEventLoopGroup();
        }
        return new NioEventLoopGroup();
    }

    @Bean
    DefaultEventLoopGroup workerGroup()
    {
        return new DefaultEventLoopGroup();
    }


    @Bean
    EventLoopGroup childGroup()
    {
        if (channelType.equals(ChannelType.NIO))
        {
            return new NioEventLoopGroup();
        } else if (channelType.equals(ChannelType.EPOLL)) {
            return new EpollEventLoopGroup();
        }
        return new NioEventLoopGroup();
    }


}
