package com.example.handler;

import com.example.Util.EncryptionUtil;
import com.example.Util.MessageUtil;
import com.example.protocol.BinaryMessage;
import com.example.protocol.Enums.EncryptionMethod;
import com.example.protocol.HeaderMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

public class EncryptionHandler  extends MessageToMessageCodec<BinaryMessage, BinaryMessage> {


    public static final String Encryption_Header_Name="EncryptionMethod";

    EncryptionUtil encryptionUtil;


    HeaderMap headerMap;

    public EncryptionHandler(EncryptionUtil encryptionUtil, HeaderMap headerMap) {
        this.encryptionUtil = encryptionUtil;
        this.headerMap = headerMap;
    }

    /**
     * @param ctx 处理器上下文
     * @param msg 未加密的报文
     * @param out 加密后的报文去处
     * @throws Exception 异常
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, BinaryMessage msg, List<Object> out) throws Exception {
        EncryptionMethod method = EncryptionMethod.forInteger(HeaderMap.getHeaderValue(msg.getHeaderMap(), EncryptionMethod.class.getCanonicalName()));
        msg.setContent(encryptionUtil.encode(method,msg.getContent()));
        msg.setSize(MessageUtil.countSize(msg));
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
        EncryptionMethod method = EncryptionMethod.forInteger(HeaderMap.getHeaderValue(msg.getHeaderMap(), EncryptionMethod.class.getCanonicalName()));
        msg.setContent(encryptionUtil.decode(method, msg.getContent()));
        out.add(msg);
    }
}
