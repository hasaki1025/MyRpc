package com.example.handler;

import com.example.Util.ChannelUtil;
import com.example.net.ResponseMap;
import com.example.protocol.Enums.MessageType;
import com.example.protocol.Message;
import com.example.protocol.RPCResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



@ChannelHandler.Sharable
@Slf4j
@Order(4)
public class CallServiceResponseHandler extends SimpleChannelInboundHandler<RPCResponse> {





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