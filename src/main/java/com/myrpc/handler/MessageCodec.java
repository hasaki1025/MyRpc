package com.myrpc.handler;

import com.myrpc.exception.MessageReadException;
import com.myrpc.Util.ChannelUtil;
import com.myrpc.net.ResponseMap;
import com.myrpc.protocol.BinaryMessage;
import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.Util.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import java.util.List;

@ChannelHandler.Sharable
@Slf4j
@Order(1)
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

            BinaryMessage message = MessageUtil.bytesToBinaryMessage(msg);
            if (message.getMessageType().equals(MessageType.response))
            {
                ResponseMap responseMap = ChannelUtil.getChannelResponseMap(ctx);
                //仅仅只是序号检查
                int seq = message.getSeq();
                //检查该序号对应的请求是否等待
                if (responseMap.stillWaiting(seq)) {
                    out.add(message);
                }
            }
            else {
                out.add(message);
            }
        } catch (MessageReadException e) {
            throw new RuntimeException(e);
        }
    }
}