package com.myrpc.net;

import com.myrpc.context.RpcProperties;
import com.myrpc.context.RpcServiceContext;
import com.myrpc.protocol.Enums.ChannelType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
@Data
public class Server implements Closeable {


    final EventLoopGroup bossGroup;

    final EventLoopGroup childGroup;

    final DefaultEventLoopGroup workerGroup;

    ServerChannel serverChannel;

    final ChannelType channelType;

    final RpcServiceChannelInitializer rpcServiceChannelInitializer;

    final RpcProperties rpcProperties;

    String ip;
    int port;

    public Server(EventLoopGroup bossGroup, EventLoopGroup childGroup, DefaultEventLoopGroup workerGroup, ChannelType channelType, List<ChannelHandler> handlers, RpcProperties rpcProperties, int port) throws Exception {
        this.bossGroup = bossGroup;
        this.childGroup = childGroup;
        this.workerGroup = workerGroup;
        this.channelType = channelType;
        this.rpcServiceChannelInitializer = new RpcServiceChannelInitializer(handlers,rpcProperties.getRpcNetProperties().getRequestTimeOut());
        this.rpcProperties = rpcProperties;
        this.port = port;
        init();
    }


    public void init() {
        try {
            new ServerBootstrap()
                    .group(bossGroup, childGroup)
                    .channel(ChannelType.ToServerChannelClass(channelType))
                    .childHandler(rpcServiceChannelInitializer)
                    .bind(port).addListener((ChannelFutureListener) future -> {
                        log.info("Server [{}] start....", future.channel().localAddress());
                        if (future.channel() instanceof NioServerSocketChannel) {
                            serverChannel = (NioServerSocketChannel) future.channel();
                        }
                        serverChannelInit();
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    protected void serverChannelInit() throws Exception {
        InetSocketAddress localAddress = (InetSocketAddress) serverChannel.localAddress();
        ip = localAddress.getHostName();
        port = localAddress.getPort();
        rpcProperties.getRpcNetProperties().setPort(port);
    }

    @Override
    public void close() throws IOException {

        serverChannel.close().addListener(f -> {
            log.info("server[{}] close....", ip + ":" + port);
        });

    }
}
