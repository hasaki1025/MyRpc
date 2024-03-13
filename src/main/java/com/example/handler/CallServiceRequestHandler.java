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
        /*CallServicesResponse response = new CallServicesResponse();
        //TODO 在此处获取Mapping对应的方法和参数等信息
        try {
            log.info("get Call ServicesRequest:{}",msg);
            CallServicesRequest request = msg.content();
            response.setResult(provider.invokeMapping(request.getParamValues(),request.getMapping()));
            log.info("Method Invoke successfully....");
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //并没有定义size
        ctx.writeAndFlush(
                new ResponseMessage<>(CommandType.Call, MessageType.response, response, msg.getSeq())
        );*/


    }
}