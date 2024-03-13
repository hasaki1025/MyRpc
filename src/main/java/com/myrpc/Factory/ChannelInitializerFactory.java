package com.myrpc.Factory;

import com.myrpc.handler.CallServiceRequestHandler;
import com.myrpc.handler.CallServiceResponseHandler;
import com.myrpc.net.ClientChannelInitializer;
import com.myrpc.net.RpcServiceChannelInitializer;
import io.netty.channel.ChannelHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
public class ChannelInitializerFactory {


    final List<ChannelHandler> handlers;


    final long timeout;

    public ChannelInitializerFactory(List<ChannelHandler> handlers,@Value("${MyRpc.net.RequestTimeOut}") long timeout) {
        this.handlers = handlers;
        this.timeout = timeout;
    }


    @Bean
    ClientChannelInitializer clientChannelInitializer()
    {
        LinkedList<ChannelHandler> list = new LinkedList<>();
        handlers.forEach(
                handler->{
                    if (!(handler instanceof CallServiceRequestHandler))
                        list.add(handler);
                }
        );
        return new ClientChannelInitializer(list, timeout);
    }



    @Bean
    RpcServiceChannelInitializer rpcServiceChannelInitializer()
    {
        LinkedList<ChannelHandler> list = new LinkedList<>();
        handlers.forEach(
                handler->{
                    if (!(handler instanceof CallServiceResponseHandler))
                        list.add(handler);
                }
        );
        return new RpcServiceChannelInitializer(list, timeout);
    }
}
