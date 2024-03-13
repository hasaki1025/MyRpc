package com.example.handler;

import com.example.Factory.RpcResponseFactory;
import com.example.Util.ChannelUtil;
import com.example.Util.ReflectInvoker;
import com.example.context.RpcServiceContext;
import com.example.net.ResponseMap;
import com.example.protocol.Enums.MessageType;
import com.example.protocol.Message;
import com.example.protocol.RPCRequest;
import com.example.protocol.RPCResponse;
import com.example.protocol.content.ResponseContent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;


@ChannelHandler.Sharable
@Slf4j
@Order(4)
public class CallServiceRequestHandler extends SimpleChannelInboundHandler<RPCRequest> {

    RpcServiceContext context;



    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getMessageType().equals(MessageType.request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,RPCRequest msg) throws Exception {
        Object object = context.getLocalServiceObject(msg.getContent().getServiceName());
        ResponseContent responseContent = ReflectInvoker.invoke(object, msg.getContent());
        RPCResponse response = RpcResponseFactory.createResponse(responseContent,msg.getSeq() , context);
        ctx.writeAndFlush(response);
    }
}