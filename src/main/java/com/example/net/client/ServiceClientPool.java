package com.example.net.client;

import com.example.Util.IPUtil;
import com.example.net.ClientChannelInitializer;
import com.example.net.ResponseMap;
import com.example.protocol.Enums.ChannelType;
import com.example.protocol.RPCRequest;
import com.example.protocol.content.ResponseContent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
//TODO 需要特殊注入
public class ServiceClientPool {


    final EventLoopGroup group;

    final DefaultEventLoopGroup workerGroup;
    final ClientChannelInitializer channelInitializer;

    final  List<ChannelHandler> handlersChain;

    final ChannelType channelType;

    public ServiceClientPool(EventLoopGroup group, DefaultEventLoopGroup workerGroup, List<ChannelHandler> handlersChain, ChannelType channelType, long timeout) {
        this.group = group;
        this.workerGroup = workerGroup;
        this.handlersChain = handlersChain;
        this.channelType = channelType;
        this.timeout = timeout;
        channelInitializer=new ClientChannelInitializer(handlersChain,timeout);
    }

    long timeout;


    /**
     * key为client地址（ip+port），value为ServiceClient队列
     */
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<ServiceClient>> clientMap=new ConcurrentHashMap<>();


    public ServiceClient getConnection(String address)
    {
        ConcurrentLinkedQueue<ServiceClient> queue = clientMap.computeIfAbsent(address,(addr)->new ConcurrentLinkedQueue<>());
        ServiceClient client = queue.poll();
        if (client==null)
        {
            return createConnection(address);
        }
        return client;
    }

    public ServiceClient createConnection(String address)
    {
        ServiceClient serviceClient = new ServiceClient(group, workerGroup, channelInitializer, timeout);
        String[] split = address.split(":");
        serviceClient.init(split[0],Integer.parseInt(split[1]),ChannelType.ToChannelClass(channelType));
        return serviceClient;
    }



}
