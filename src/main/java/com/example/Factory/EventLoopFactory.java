package com.example.Factory;

import com.example.protocol.Enums.ChannelType;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventLoopFactory {

    @Value("${MyRpc.net.ChannelType}")
    String channelType;
    @Bean
    EventLoopGroup group()
    {
        if (channelType.equals(ChannelType.NIO.name()))
        {
            return new NioEventLoopGroup();
        } else if (channelType.equals(ChannelType.EPOLL.name())) {
            return new EpollEventLoopGroup();
        }
        return new NioEventLoopGroup();
    }

    @Bean
    DefaultEventLoopGroup workerGroup()
    {
        return new DefaultEventLoopGroup();
    }


}
