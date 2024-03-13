package com.example.Factory;

import com.example.net.ClientChannelInitializer;
import com.example.net.client.ServiceClientPool;
import com.example.protocol.Enums.ChannelType;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceClientPoolFactory implements FactoryBean<ServiceClientPool> {

    String channelType;

    EventLoopGroup eventLoopGroup;
    DefaultEventLoopGroup defaultEventLoopGroup;

    ClientChannelInitializer clientChannelInitializer;


    public ServiceClientPoolFactory(@Value("${MyRpc.net.ChannelType}") String channelType,
                                    @Qualifier("group") EventLoopGroup eventLoopGroup,
                                    @Qualifier("workerGroup")DefaultEventLoopGroup defaultEventLoopGroup,
                                    ClientChannelInitializer clientChannelInitializer) {
        this.channelType = channelType;
        this.eventLoopGroup = eventLoopGroup;
        this.defaultEventLoopGroup = defaultEventLoopGroup;
        this.clientChannelInitializer = clientChannelInitializer;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public ServiceClientPool getObject() throws Exception {
        return new ServiceClientPool(eventLoopGroup,
                clientChannelInitializer,
                clientChannelInitializer.getHandlersChain(),
                ChannelType.valueOf(channelType),
                clientChannelInitializer.getTimeout()
                );
    }

    /**
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return ServiceClientPool.class;
    }

    /**
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
