package com.myrpc.Util;

import com.myrpc.net.ResponseMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class ChannelUtil {

    public static final String CHANNEL_RESPONSE_MAP="ChannelResponseMap";

    public static final String TIME_OUT_KEY="requestTimeOut";


    public static void initChannelAttribute(Channel channel,long time)
    {
        setTimeOut(channel,time);
        setChannelResponseMap(channel);
    }


    public static void setTimeOut(Channel channel,long time)
    {
        Attribute<Object> attr = channel.attr(AttributeKey.valueOf(TIME_OUT_KEY));
        attr.set(time);
    }

    public static long getTimeOUT(Channel channel)
    {
        return (long) channel.attr(AttributeKey.valueOf(TIME_OUT_KEY)).get();
    }



    public static ResponseMap getChannelResponseMap(ChannelHandlerContext ctx)
    {
        Attribute<Object> attr = ctx.channel().attr(AttributeKey.valueOf(CHANNEL_RESPONSE_MAP));
        return (ResponseMap) attr.get();
    }


    public static ResponseMap getChannelResponseMap(Channel channel)
    {
        Attribute<Object> attr = channel.attr(AttributeKey.valueOf(CHANNEL_RESPONSE_MAP));
        return (ResponseMap) attr.get();
    }

    public static void setChannelResponseMap(Channel channel)
    {
        Attribute<Object> attr = channel.attr(AttributeKey.valueOf(CHANNEL_RESPONSE_MAP));
        attr.set(new ResponseMap(getTimeOUT(channel)));
    }
}
