package com.example.net.client;


import com.example.Util.ChannelUtil;
import com.example.net.ClientChannelInitializer;
import com.example.protocol.RPCRequest;
import com.example.protocol.content.ResponseContent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务消费者连接到服务提供者
 */
@Slf4j
@Getter
public class ServiceClient implements Closeable {



    final EventLoopGroup group;

    final DefaultEventLoopGroup workerGroup;
    Channel channel;
    final ClientChannelInitializer channelInitializer;

    String remoteIPAddress;
    int remotePort;





    AtomicBoolean isInit=new AtomicBoolean(false);


    public ServiceClient(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlers) {
        this.group = group;
        this.workerGroup = workerGroup;
        this.channelInitializer = new ClientChannelInitializer(handlers);
    }

    /**
     * netty客户端初始化
     * @param host 连接IP地址
     * @param port 连接端口
     * @param channelClass 信道类型
     */
    public void init(String host, int port, Class<? extends Channel> channelClass) {
        if (isInit.compareAndSet(false,true))
        {
            try {
                channel = new Bootstrap()
                        .group(group)
                        .channel(channelClass)
                        .handler(channelInitializer).connect(host, port).sync().channel();
                remoteIPAddress=host;
                remotePort=port;
                log.info("ServiceClient [{}:{}] initialized.",remoteIPAddress,remotePort);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            log.warn("ServiceClient[{}:{}] is already initialized.",remoteIPAddress,remotePort);
        }
    }


    /**
     * 采用默认的同步调用
     * @param request 请求
     * @return 请求的响应content
     */
    public Future<ResponseContent> call(RPCRequest request)
    {
        channel.writeAndFlush(request);
        return ChannelUtil.getChannelResponseMap(channel).addWaitingRequest(request.getSeq());
    }


    public boolean isInit()
    {
        return isInit.get();
    }


    /**
     * 关闭当前连接
     * @throws IOException 连接关闭抛出
     */
    @Override
    public void close() throws IOException {
        channel.close().addListener((ChannelFutureListener) future -> {
            log.info("ServiceClient[{}:{}] connect close.....",remoteIPAddress,remotePort);
        });
    }




}
