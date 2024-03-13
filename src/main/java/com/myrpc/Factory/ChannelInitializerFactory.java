package com.myrpc.Factory;

import com.myrpc.context.RpcProperties;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.handler.*;
import com.myrpc.net.ClientChannelInitializer;
import com.myrpc.net.EncipherConvertor;
import com.myrpc.net.RpcServiceChannelInitializer;
import com.myrpc.net.SerializableConvertor;
import io.netty.channel.ChannelHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class ChannelInitializerFactory {


    final RpcProperties rpcProperties;


    final EncipherConvertor encipherConvertor;
    final SerializableConvertor serializableConvertor;

    final  RpcResponseFactory rpcResponseFactory;

    final RpcServiceContext context;

    final long timeout;

    List<ChannelHandler> clientHandlerChain=new ArrayList<>();
    List<ChannelHandler> serverHandlerChain=new ArrayList<>();

    public ChannelInitializerFactory(RpcProperties rpcProperties,
                                     EncipherConvertor encipherConvertor,
                                     SerializableConvertor serializableConvertor,
                                     RpcResponseFactory rpcResponseFactory,
                                     RpcServiceContext context) throws Exception {
        this.rpcProperties = rpcProperties;
        this.encipherConvertor = encipherConvertor;
        this.serializableConvertor = serializableConvertor;
        this.rpcResponseFactory = rpcResponseFactory;
        this.context = context;
        this.timeout = rpcProperties.getRpcNetProperties().getRequestTimeOut();
        init();
    }



    public void init()
    {
        LinkedList<ChannelHandler> list = new LinkedList<>();
        clientHandlerChain.add(new MessageCodec());
        serverHandlerChain.add(new MessageCodec());

        clientHandlerChain.add(new EncryptionHandler(encipherConvertor));
        serverHandlerChain.add(new EncryptionHandler(encipherConvertor));

        clientHandlerChain.add(new RequestSerializableHandler(serializableConvertor,rpcProperties));
        serverHandlerChain.add(new RequestSerializableHandler(serializableConvertor,rpcProperties));
        clientHandlerChain.add(new ResponseSerializableHandler(serializableConvertor,rpcProperties));
        serverHandlerChain.add(new ResponseSerializableHandler(serializableConvertor,rpcProperties));

        serverHandlerChain.add(new CallServiceRequestHandler(rpcResponseFactory,context));
        clientHandlerChain.add(new CallServiceResponseHandler());
    }



    @Bean
    ClientChannelInitializer clientChannelInitializer()
    {
        return new ClientChannelInitializer(clientHandlerChain, timeout);
    }



    @Bean
    RpcServiceChannelInitializer rpcServiceChannelInitializer()
    {
        return new RpcServiceChannelInitializer(serverHandlerChain, timeout);
    }
}
