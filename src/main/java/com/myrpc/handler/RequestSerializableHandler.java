package com.myrpc.handler;

import com.myrpc.context.ProtocolProperties;
import com.myrpc.context.RpcProperties;
import com.myrpc.net.SerializableConvertor;
import com.myrpc.protocol.BinaryMessage;
import com.myrpc.protocol.Enums.MessageType;
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
@Component("com.example.handler.RequestSerializableHandler")
public class RequestSerializableHandler extends MessageToMessageCodec<BinaryMessage, RPCRequest> {


    final SerializableConvertor serializableConvertor;
    private final RpcProperties rpcProperties;

    public RequestSerializableHandler(SerializableConvertor serializableConvertor, RpcProperties rpcProperties) {
        this.serializableConvertor = serializableConvertor;
        this.rpcProperties = rpcProperties;
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
            byte[] bytesContent = serializableConvertor.serialize(msg.getContent());
            ProtocolProperties protocolProperties=rpcProperties.getRpcNetProperties().getProtocolProperties();
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
            RequestContent content = (RequestContent) serializableConvertor.deserialize( msg.getContent(),true);
            out.add(msg.toRPCRequest(content));
        }
        else {
            out.add(msg);
        }

    }

}
