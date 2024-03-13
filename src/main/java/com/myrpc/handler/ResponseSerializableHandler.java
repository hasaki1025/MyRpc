package com.myrpc.handler;

import com.myrpc.net.SerializableConvertor;
import com.myrpc.protocol.BinaryMessage;
import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.protocol.RPCResponse;
import com.myrpc.protocol.content.ResponseContent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@ChannelHandler.Sharable
@Order(3)
@Slf4j
@Component("com.example.handler.ResponseSerializableHandler")
public class ResponseSerializableHandler extends MessageToMessageCodec<BinaryMessage, RPCResponse> {


    SerializableConvertor  serializableConvertor;


    public ResponseSerializableHandler(SerializableConvertor serializableConvertor) {
        this.serializableConvertor = serializableConvertor;
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
            byte[] bytesContent = serializableConvertor.serialize( msg.getContent());
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
            ResponseContent content = (ResponseContent) serializableConvertor.deserialize( msg.getContent(),false);
            out.add(msg.toRPCResponse(content));
        }
        else {
            out.add(msg);
        }

    }

}