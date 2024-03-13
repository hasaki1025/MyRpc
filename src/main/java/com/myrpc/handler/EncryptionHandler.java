package com.myrpc.handler;


import com.myrpc.Util.MessageUtil;
import com.myrpc.net.EncipherConvertor;
import com.myrpc.protocol.BinaryMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
@ChannelHandler.Sharable
@Slf4j
@Order(2)

public class EncryptionHandler  extends MessageToMessageCodec<BinaryMessage, BinaryMessage> {


    EncipherConvertor encipherConvertor;

    /**
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("EncryptionHandler get Error {}" ,cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

    public EncryptionHandler(EncipherConvertor encipherConvertor) {
        this.encipherConvertor = encipherConvertor;
    }

    /**
     * @param ctx 处理器上下文
     * @param msg 未加密的报文
     * @param out 加密后的报文去处
     * @throws Exception 异常
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, BinaryMessage msg, List<Object> out) throws Exception {
        msg.setContent(encipherConvertor.encode(msg.getContent()));
        out.add(msg);

    }

    /**
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryMessage msg, List<Object> out) throws Exception {
        msg.setContent(encipherConvertor.decode( msg.getContent()));
        msg.setSize(MessageUtil.countSize(msg));
        out.add(msg);
    }
}
