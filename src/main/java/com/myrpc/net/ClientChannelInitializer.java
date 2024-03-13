package com.myrpc.net;

import com.myrpc.Util.ChannelUtil;
import com.myrpc.Util.MessageUtil;
import com.myrpc.context.RpcProperties;
import com.myrpc.context.SSLProperties;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.util.List;

@Slf4j
@Getter
@ChannelHandler.Sharable
public class ClientChannelInitializer extends ChannelInitializer<Channel> {




    /**
     * 注入的处理器链
     */
    List<ChannelHandler> handlersChain;

    long timeout;

    boolean enableSSL;



    RpcProperties rpcProperties;


    /**
     * @param handlersChain
     * @param enableSSL 注意这里的是否开启SSL取决于连接的服务器是否需要SSL验证
     * @param rpcProperties
     * @throws Exception
     */
    public ClientChannelInitializer(List<ChannelHandler> handlersChain, boolean enableSSL, RpcProperties rpcProperties) throws Exception {
        this.handlersChain = handlersChain;
        this.timeout = rpcProperties.getRpcNetProperties().getRequestTimeOut();
        this.enableSSL=enableSSL;
        this.rpcProperties=rpcProperties;
    }

    /**
     * 初始化信道
     * @param ch 信道
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        log.info("channel {}-->{} initialized",ch.localAddress(),ch.remoteAddress());
        ChannelUtil.initChannelAttribute(ch,timeout);
        ChannelPipeline pipeline = ch.pipeline();
        if (enableSSL)
        {
            SSLProperties sslProperties = rpcProperties.getRpcNetProperties().getSslProperties();
            File caCrt = sslProperties.getCaCrtResource().getFile();
            File localCrt = sslProperties.getServerCRTResource().getFile();
            File privateKey = sslProperties.getPrivateKeyResource().getFile();
            SslContext build = SslContextBuilder.forClient()
                    .keyManager(localCrt, privateKey)
                    .trustManager(caCrt)
                    .build();
            pipeline.addLast(build.newHandler(ch.alloc()));
        }
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, MessageUtil.CONST_MESSAGE_HEAD_LENGTH,MessageUtil.LENGTH_FIELD_SIZE,0,0));
        pipeline.addLast(handlersChain.toArray(new ChannelHandler[0]));
        log.info("Server Connect to {}",ch.remoteAddress());
    }
}
