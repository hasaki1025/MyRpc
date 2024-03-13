package com.example.handler;

import com.example.protocol.Enums.MessageType;
import com.example.protocol.Message;
import com.example.protocol.RPCRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;


@Slf4j
@ChannelHandler.Sharable
public class CallServiceRequestHandler extends SimpleChannelInboundHandler<RPCRequest> {





    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getMessageType().equals(MessageType.request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,RPCRequest msg) {

    }
}