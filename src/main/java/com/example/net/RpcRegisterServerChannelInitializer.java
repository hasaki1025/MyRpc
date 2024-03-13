package com.example.net;


import com.example.context.RpcContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;

import java.util.List;

/**
 * 注册中心信道处理器
 */
public class RpcRegisterServerChannelInitializer extends RpcServerChannelInitializer {



    /**
     * 工作线程池
     */
    DefaultEventLoopGroup workerGroup;


    /**
     * 心跳检测间隔
     */
    int heartCheckTime;


    /**
     * 注册中心上下文
     */
    RpcContext context;


    /**
     * 初始化信道
     * @param handlersChain handler链
     * @param workerGroup 工作线程池
     * @param heartCheckTime 心跳检测间隔
     * @param register 注册中心
     */
    public RpcRegisterServerChannelInitializer(List<ChannelHandler> handlersChain, DefaultEventLoopGroup workerGroup, int heartCheckTime, RpcContext context) {
        super(handlersChain);
        this.workerGroup = workerGroup;
        this.heartCheckTime = heartCheckTime;
        this.context = context;
    }



}
