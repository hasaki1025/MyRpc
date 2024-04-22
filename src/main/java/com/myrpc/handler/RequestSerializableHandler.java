package com.myrpc.handler;

import com.myrpc.context.ProtocolProperties;
import com.myrpc.context.RpcProperties;
import com.myrpc.net.SerializableConvertor;
import com.myrpc.net.SerializableConvertorMap;
import com.myrpc.protocol.BinaryMessage;
import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.protocol.Message;
import com.myrpc.protocol.RPCRequest;
import com.myrpc.protocol.content.RequestContent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.CacheResponse;
import java.util.List;


@ChannelHandler.Sharable
@Order(3)
@Slf4j

public class RequestSerializableHandler extends MessageToMessageCodec<BinaryMessage, RPCRequest> {


    final SerializableConvertorMap serializableConvertorMap;



    /**
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("RequestSerializableHandler get Error {}" ,cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }


    public RequestSerializableHandler(SerializableConvertorMap serializableConvertorMap) {
        this.serializableConvertorMap = serializableConvertorMap;
    }

    @Override
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return super.acceptOutboundMessage(msg) && ((Message)msg).getMessageType().equals(MessageType.request);
    }
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message)msg).getMessageType().equals(MessageType.request);
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, RPCRequest msg, List<Object> out) throws Exception {
        if (msg.getMessageType().equals(MessageType.request))
        {
            log.info("send {} Request",msg.getMessageType().name());
            byte[] bytesContent = serializableConvertorMap.serialize(msg.getContent(),msg.getSerializableType());
            out.add(msg.toBinaryMessage(bytesContent));
        }
        else {
            out.add(msg);
        }

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryMessage msg, List<Object> out) throws Exception {
        if(MessageType.request.equals(msg.getMessageType()))
        {
            log.info("get {} Request",msg.getMessageType().name());
            RequestContent content =  serializableConvertorMap.deserializeRequestContent( msg.getContent(),msg.getSerializableType());
            out.add(msg.toRPCRequest(content));
        }
        else {
            out.add(msg);
        }

    }

}
