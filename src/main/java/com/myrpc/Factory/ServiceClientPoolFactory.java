package com.myrpc.Factory;

import com.myrpc.context.RpcProperties;
import com.myrpc.net.ClientChannelInitializer;
import com.myrpc.net.client.ServiceClientPool;
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
public class ServiceClientPoolFactory  {

    ChannelType channelType;

    EventLoopGroup eventLoopGroup;
    DefaultEventLoopGroup defaultEventLoopGroup;
    List<ChannelHandler> handlers;
    long timout;


    public ServiceClientPoolFactory(RpcProperties rpcProperties,
                                    @Qualifier("group") EventLoopGroup eventLoopGroup,
                                    @Qualifier("workerGroup")DefaultEventLoopGroup defaultEventLoopGroup,
                                    List<ChannelHandler> handlers) throws Exception {
        this.channelType = rpcProperties.getRpcNetProperties().getChannelType();
        this.eventLoopGroup = eventLoopGroup;
        this.defaultEventLoopGroup = defaultEventLoopGroup;
        this.handlers = handlers;
        this.timout=rpcProperties.getRpcNetProperties().getRequestTimeOut();
    }

    /**
     * @return
     * @throws Exception
     */
    @Bean
    public ServiceClientPool serviceClientPool() throws Exception {
        return new ServiceClientPool(eventLoopGroup,
                handlers,
                channelType,
                timout
                );
    }


}
