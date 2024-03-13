package com.example.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Closeable;
import java.io.IOException;

@Slf4j
public class Server implements Closeable {


    EventLoopGroup bossGroup;

    EventLoopGroup chirdGroup;

    DefaultEventLoopGroup workerGroup;

    ServerChannel serverChannel;


    public final static ChannelGroup CHANNEL_CONSUMER_GROUP=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    public static boolean containConsumerChannnel(Channel channel)
    {
        return CHANNEL_CONSUMER_GROUP.contains(channel);
    }

    public static void addConsumerChannel(Channel channel)
    {
        CHANNEL_CONSUMER_GROUP.add(channel);
    }




    void init(int port,RpcServerChannelInitializer channelInitializer)
    {
        try {
            new ServerBootstrap()
                    .group(bossGroup,chirdGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .bind(port).addListener((ChannelFutureListener) future -> {
                        log.info("Server [{}] start....",future.channel().localAddress());
                        if(future.channel() instanceof NioServerSocketChannel)
                        {
                            serverChannel= (NioServerSocketChannel) future.channel();
                        }
                        serverChannelInit();
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    protected void serverChannelInit() {
        //NOOP
    }

    @Override
    public void close() throws IOException {
        try {
            serverChannel.close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
