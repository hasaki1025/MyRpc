package com.example.handler;

import com.example.protocol.Enums.MessageType;
import com.example.protocol.Message;
import com.example.protocol.RPCResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



@ChannelHandler.Sharable
@Slf4j
public class CallServiceResponseHandler extends SimpleChannelInboundHandler<RPCResponse> {





    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && ((Message) msg).getMessageType().equals(MessageType.response);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponse msg) throws Exception {
      /*  try {
            int seq = msg.getSeq();
            log.info("[{}] Request get Response", seq);
            if (msg.content().hasException()) {
                msg.content().getException().printStackTrace();
            }else {
                ChannelResponse mapResponse = (ChannelResponse) ctx.channel().attr(AttributeKey.valueOf(MessageUtil.CHANNEL_RESPONSE_MAP)).get();
                CallResponseAction action = (CallResponseAction) mapResponse.getResponseAction(seq);
                //是否是同步请求
                if (!action.hasThreadWaiting())
                {
                    //异步请求，另起线程执行该操作
                    action.setContent(msg.content());
                    threadPool.submit(action::action);
                }
                else {
                    mapResponse.putResponseContent(seq,msg.content());
                }
                mapResponse.removeResponseAction(seq);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
*/
    }
}