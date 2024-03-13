package com.myrpc.Factory;

import com.myrpc.context.RpcProperties;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.handler.*;
import com.myrpc.net.*;
import io.netty.channel.ChannelHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ChannelInitializerHandlerFactory {


    final RpcProperties rpcProperties;


   final EncipherConvertorMap encipherConvertorMap;
   final SerializableConvertorMap serializableConvertorMap;

    final  RpcResponseFactory rpcResponseFactory;

    final RpcServiceContext context;

    final long timeout;

    List<ChannelHandler> clientHandlerChain=new ArrayList<>();
    List<ChannelHandler> serverHandlerChain=new ArrayList<>();

    final ResourceLoader resourceLoader;


    public ChannelInitializerHandlerFactory(RpcProperties rpcProperties,
                                            EncipherConvertorMap encipherConvertorMap,
                                            SerializableConvertorMap serializableConvertorMap,
                                            RpcResponseFactory rpcResponseFactory,
                                            RpcServiceContext context, ResourceLoader resourceLoader) throws Exception {
        this.rpcProperties = rpcProperties;
        this.encipherConvertorMap = encipherConvertorMap;
        this.serializableConvertorMap = serializableConvertorMap;
        this.rpcResponseFactory = rpcResponseFactory;
        this.context = context;
        this.timeout = rpcProperties.getRpcNetProperties().getRequestTimeOut();
        this.resourceLoader = resourceLoader;
        init();
    }

    public void init() throws Exception {
        MessageCodec messageCodec = new MessageCodec();

        EncryptionHandler encryptionHandler = new EncryptionHandler(encipherConvertorMap);

        RequestSerializableHandler requestSerializableHandler = new RequestSerializableHandler(serializableConvertorMap, rpcProperties);

        ResponseSerializableHandler responseSerializableHandler = new ResponseSerializableHandler(serializableConvertorMap, rpcProperties);

        CallServiceRequestHandler callServiceRequestHandler = new CallServiceRequestHandler(rpcResponseFactory, context);

        CallServiceResponseHandler callServiceResponseHandler = new CallServiceResponseHandler();

        clientHandlerChain.add(messageCodec);
        clientHandlerChain.add(encryptionHandler);
        clientHandlerChain.add(requestSerializableHandler);
        clientHandlerChain.add(responseSerializableHandler);
        clientHandlerChain.add(callServiceResponseHandler);

        serverHandlerChain.add(messageCodec);
        serverHandlerChain.add(encryptionHandler);
        serverHandlerChain.add(requestSerializableHandler);
        serverHandlerChain.add(responseSerializableHandler);
        serverHandlerChain.add(callServiceRequestHandler);
    }



    @Bean("SSLClientChannelInitializer")
    ClientChannelInitializer sslClientChannelInitializer() throws Exception {
        return new ClientChannelInitializer(clientHandlerChain,true,rpcProperties);
    }


    @Bean("CommonClientChannelInitializer")
    ClientChannelInitializer commonClientChannelInitializer() throws Exception {
        return new ClientChannelInitializer(clientHandlerChain,false,rpcProperties);
    }

    @Bean("serviceChannelInitializer")
    RpcServiceChannelInitializer serviceChannelInitializer() throws Exception {
        return new RpcServiceChannelInitializer(serverHandlerChain,rpcProperties);
    }

}
