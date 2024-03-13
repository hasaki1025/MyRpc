package com.myrpc.Factory;

import com.myrpc.context.RpcProperties;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.handler.*;
import com.myrpc.net.ClientChannelInitializer;
import com.myrpc.net.EncipherConvertor;
import com.myrpc.net.RpcServiceChannelInitializer;
import com.myrpc.net.SerializableConvertor;
import io.netty.channel.ChannelHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class ChannelChainFactory {


    final RpcProperties rpcProperties;


    final EncipherConvertor encipherConvertor;
    final SerializableConvertor serializableConvertor;

    final  RpcResponseFactory rpcResponseFactory;

    final RpcServiceContext context;

    final long timeout;

    List<ChannelHandler> handlerChain=new ArrayList<>();

    public ChannelChainFactory(RpcProperties rpcProperties,
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
        MessageCodec messageCodec = new MessageCodec();
        handlerChain.add(messageCodec);
        EncryptionHandler encryptionHandler = new EncryptionHandler(encipherConvertor);
        handlerChain.add(encryptionHandler);
        RequestSerializableHandler requestSerializableHandler = new RequestSerializableHandler(serializableConvertor, rpcProperties);
        handlerChain.add(requestSerializableHandler);
        ResponseSerializableHandler responseSerializableHandler = new ResponseSerializableHandler(serializableConvertor, rpcProperties);
        handlerChain.add(responseSerializableHandler);
        handlerChain.add(new CallServiceRequestHandler(rpcResponseFactory,context));
        handlerChain.add(new CallServiceResponseHandler());
    }



    @Bean
    List<ChannelHandler> handlerChain()
    {
        return handlerChain;
    }

}
