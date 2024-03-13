package com.myrpc.Factory;

import com.myrpc.net.ProviderServer;
import com.myrpc.net.RpcServiceChannelInitializer;
import com.myrpc.protocol.Enums.ChannelType;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProviderServerFactory implements FactoryBean<ProviderServer> {



    String channelType;
    int port;

    EventLoopGroup group;
    EventLoopGroup childGroup;
    DefaultEventLoopGroup workerGroup;
    RpcServiceChannelInitializer rpcServiceChannelInitializer;

    public ProviderServerFactory( @Value("${MyRpc.net.ChannelType}") String channelType,
                                  @Value("${MyRpc.service.port}") String port,
                                  @Qualifier("group") EventLoopGroup group,
                                  @Qualifier("childGroup")EventLoopGroup childGroup,
                                  @Qualifier("workerGroup") DefaultEventLoopGroup workerGroup,
                                  RpcServiceChannelInitializer rpcServiceChannelInitializer) {
        this.channelType = channelType;
        this.port = Integer.parseInt(port);
        this.group = group;
        this.childGroup = childGroup;
        this.workerGroup = workerGroup;
        this.rpcServiceChannelInitializer = rpcServiceChannelInitializer;
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public ProviderServer getObject() throws Exception {
        return new ProviderServer(group,childGroup,workerGroup, ChannelType.valueOf(channelType),rpcServiceChannelInitializer,port);
    }

    /**
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return ProviderServer.class;
    }

    /**
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
