package com.example.handler;

import com.example.exception.MessageReadException;
import com.example.Util.ChannelUtil;
import com.example.net.ResponseMap;
import com.example.protocol.BinaryMessage;
import com.example.protocol.Enums.MessageType;
import com.example.Util.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;


import java.util.List;

@ChannelHandler.Sharable
@Slf4j
public class MessageCodec extends MessageToMessageCodec<ByteBuf, BinaryMessage> {




    @Override
    protected void encode(ChannelHandlerContext ctx, BinaryMessage msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        MessageUtil.messageToBytes(msg,buffer);
        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {

            BinaryMessage message = MessageUtil.bytesToDefaultMessage(msg);
            if (message.getMessageType().equals(MessageType.response))
            {
                ResponseMap responseMap = ChannelUtil.getChannelResponseMap(ctx);
                //仅仅只是序号检查
                int seq = message.getSeq();
                if (!responseMap.stillWaiting(seq)) {
                    throw new RuntimeException("not match Request of this Response");
                }
            }
            out.add(message);
        } catch (MessageReadException e) {
            throw new RuntimeException(e);
        }
    }
}