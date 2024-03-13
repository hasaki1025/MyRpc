package com.myrpc.protocol.Enums;

import com.myrpc.net.LoadBalance.LoadBalancePolicy;
import com.myrpc.net.client.NacosClient;
import io.netty.channel.Channel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public enum LoadBalancePolicyType {


    WeightedRoundRobin(0),WeightedRandom(1),;


    private static final Class<? extends LoadBalancePolicy>[] loadBalanceClass=new Class[]{
            NacosClient.WeightedRoundRobinPolicy.class, NacosClient.WeightedRandomPolicy.class
    } ;

    public static Class<? extends LoadBalancePolicy> toLoadBalanceClass(LoadBalancePolicyType type)
    {
        return loadBalanceClass[type.value];
    }

    final int value;
    LoadBalancePolicyType(int i) {
        this.value=i;
    }
}
