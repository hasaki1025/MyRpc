package com.example.handler;

import com.example.Util.SerializableUtil;
import com.example.protocol.BinaryMessage;
import com.example.protocol.Enums.MessageType;
import com.example.protocol.RPCRequest;
import com.example.protocol.RPCResponse;
import com.example.protocol.content.RequestContent;
import com.example.protocol.content.ResponseContent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import java.util.List;

@ChannelHandler.Sharable
@Order(3)
@Slf4j

public class ResponseSerializableHandler extends MessageToMessageCodec<BinaryMessage, RPCResponse> {


    SerializableUtil serializableUtil;

    public ResponseSerializableHandler(SerializableUtil serializableUtil) {
        this.serializableUtil = serializableUtil;
    }

    @Override
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return super.acceptOutboundMessage(msg) && ((BinaryMessage)msg).getMessageType().equals(MessageType.response);
    }
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((BinaryMessage)msg).getMessageType().equals(MessageType.response);
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, RPCResponse msg, List<Object> out) throws Exception {
        if (msg.getMessageType().equals(MessageType.response))
        {
            log.info("send {} Response",msg.getMessageType().name());
            byte[] bytesContent = serializableUtil.serialize(msg.getSerializableType(), msg.getContent());
            out.add(msg.toBinaryMessage(bytesContent));
        }
        else {
            out.add(msg);
        }

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryMessage msg, List<Object> out) throws Exception {
        if(MessageType.response.equals(msg.getMessageType()))
        {
            log.info("get {} Response",msg.getMessageType().name());
            ResponseContent content = (ResponseContent) serializableUtil.deserialize(serializableUtil.getSerializableType(msg.getHeaderMap()), msg.getContent(),false);
            out.add(msg.toRPCResponse(content));
        }
        else {
            out.add(msg);
        }

    }

}
