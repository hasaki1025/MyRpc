package com.myrpc.net.client;

import com.myrpc.net.ClientChannelInitializer;
import com.myrpc.protocol.Enums.ChannelType;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;


@Slf4j
public class ServiceClientPool {


     final EventLoopGroup group;

     final DefaultEventLoopGroup workerGroup=new DefaultEventLoopGroup();
     List<ChannelHandler> channelHandlers;


    final ChannelType channelType;



    final long timeout;


    public ServiceClientPool(EventLoopGroup group, List<ChannelHandler> channelHandlers, ChannelType channelType, long timeout) {
        this.group = group;
        this.channelHandlers=channelHandlers;
        this.channelType = channelType;
        this.timeout = timeout;
    }

    /**
     * key为client地址（ip+port），value为ServiceClient队列
     */
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<ServiceClient>> clientMap=new ConcurrentHashMap<>();







    public void returnConnection(ServiceClient client)
    {
        String addr = client.getRemoteIPAddress() + ":" + client.getRemotePort();
        ConcurrentLinkedQueue<ServiceClient> queue = clientMap.get(addr);
        queue.add(client);
    }


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
        ServiceClient serviceClient = new ServiceClient(group, workerGroup, channelHandlers, timeout,this);
        String[] split = address.split(":");
        serviceClient.init(split[0],Integer.parseInt(split[1]),ChannelType.ToChannelClass(channelType));
        return serviceClient;
    }






}
