package com.example.Util;

import com.example.net.ResponseMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class ChannelUtil {

    public static ResponseMap getChannelResponseMap(ChannelHandlerContext ctx)
    {
        Attribute<Object> attr = ctx.channel().attr(AttributeKey.valueOf(ResponseMap.CHANNEL_RESPONSE_MAP));
        return (ResponseMap) attr.get();
    }


    public static ResponseMap getChannelResponseMap(Channel channel)
    {
        Attribute<Object> attr = channel.attr(AttributeKey.valueOf(ResponseMap.CHANNEL_RESPONSE_MAP));
        return (ResponseMap) attr.get();
    }
}
