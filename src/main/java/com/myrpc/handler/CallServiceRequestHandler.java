package com.myrpc.handler;

import com.myrpc.Factory.RpcResponseFactory;
import com.myrpc.Util.ReflectInvoker;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.protocol.Message;
import com.myrpc.protocol.RPCRequest;
import com.myrpc.protocol.RPCResponse;
import com.myrpc.protocol.content.ResponseContent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@ChannelHandler.Sharable
@Slf4j
@Order(4)

public class CallServiceRequestHandler extends SimpleChannelInboundHandler<RPCRequest> {



    RpcResponseFactory rpcResponseFactory;

    RpcServiceContext context;

    public CallServiceRequestHandler(RpcResponseFactory rpcResponseFactory, RpcServiceContext context) {
        this.rpcResponseFactory = rpcResponseFactory;
        this.context = context;
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getMessageType().equals(MessageType.request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,RPCRequest msg) throws Exception {
        Object object = context.getLocalServiceObject(msg.getContent().getServiceName());
        ResponseContent responseContent = ReflectInvoker.invoke(object, msg.getContent());
        RPCResponse response = rpcResponseFactory.createResponse(responseContent,msg.getSeq());
        ctx.writeAndFlush(response);
    }
}