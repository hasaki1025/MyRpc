package com.example.handler;

import com.example.Factory.RpcRequestFactory;
import com.example.Util.SerializableUtil;
import com.example.context.ProtocolProperties;
import com.example.protocol.BinaryMessage;
import com.example.protocol.Enums.MessageType;
import com.example.Util.MessageUtil;
import com.example.protocol.RPCRequest;
import com.example.protocol.content.RequestContent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;


@ChannelHandler.Sharable
@Order(2)
@Slf4j

public class RequestSerializableHandler extends MessageToMessageCodec<BinaryMessage, RPCRequest> {


    SerializableUtil serializableUtil;

    ProtocolProperties protocolProperties;

    public RequestSerializableHandler(SerializableUtil serializableUtil, ProtocolProperties protocolProperties) {
        this.serializableUtil = serializableUtil;
        this.protocolProperties = protocolProperties;
    }

    @Override
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return super.acceptOutboundMessage(msg) && ((BinaryMessage)msg).getMessageType().equals(MessageType.request);
    }
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((BinaryMessage)msg).getMessageType().equals(MessageType.request);
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, RPCRequest msg, List<Object> out) throws Exception {
        if (msg.getMessageType().equals(MessageType.request))
        {
            log.info("send {} Request",msg.getMessageType().name());
            byte[] bytesContent = serializableUtil.serialize(msg.getSerializableType(), msg.getContent());
            out.add(msg.toBinaryMessage(bytesContent,protocolProperties.getHeaders()));
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
            RequestContent content = (RequestContent) serializableUtil.deserialize(serializableUtil.getSerializableType(msg.getHeaderMap()), msg.getContent(),true);
            out.add(msg.toRPCRequest(content));
        }
        else {
            out.add(msg);
        }

    }

}
