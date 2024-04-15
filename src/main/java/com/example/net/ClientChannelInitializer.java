package com.example.net;

import com.example.Util.ChannelUtil;
import com.example.Util.MessageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Getter
public class ClientChannelInitializer extends ChannelInitializer<Channel> {


    /**
     * 注入的处理器链
     */
    List<ChannelHandler> handlersChain;

    long timeout;


    public ClientChannelInitializer(List<ChannelHandler> handlersChain,@Value("${MyRpc.net.RequestTimeOut}") long timeout) {
        this.handlersChain = handlersChain;
        this.timeout = timeout;
    }

    /**
     * 初始化信道
     * @param ch 信道
     */
    @Override
    protected void initChannel(Channel ch) {
        log.info("channel {}-->{} initialized",ch.localAddress(),ch.remoteAddress());
        ChannelUtil.initChannelAttribute(ch,timeout);
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LoggingHandler());
        pipeline.addLast(
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, MessageUtil.CONST_MESSAGE_HEAD_LENGTH,MessageUtil.LENGTH_FIELD_SIZE,0,0));
        pipeline.addLast(handlersChain.toArray(new ChannelHandler[0]));
        log.info("Server Connect to {}",ch.remoteAddress());
    }
}
