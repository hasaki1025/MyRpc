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





    RpcServiceContext context;

    public CallServiceRequestHandler( RpcServiceContext context) {
        this.context = context;
    }

    /**
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Call Request inbound error{}",cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getMessageType().equals(MessageType.request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,RPCRequest msg) throws Exception {
        Object object = context.getLocalServiceObject(msg.getContent().getServiceName());
        ResponseContent responseContent = ReflectInvoker.invoke(object, msg.getContent());
        RPCResponse response = RpcResponseFactory.createResponse(responseContent,msg);
        ctx.writeAndFlush(response);
    }
}