package com.myrpc.handler;

import com.myrpc.Util.ChannelUtil;
import com.myrpc.net.ResponseMap;
import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.protocol.Message;
import com.myrpc.protocol.RPCResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



@ChannelHandler.Sharable
@Slf4j
@Order(4)

public class CallServiceResponseHandler extends SimpleChannelInboundHandler<RPCResponse> {




    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Call Response inbound error{}",cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getMessageType().equals(MessageType.response);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponse msg) throws Exception {
        ResponseMap responseMap = ChannelUtil.getChannelResponseMap(ctx.channel());
        responseMap.setResponse(msg);
    }
}