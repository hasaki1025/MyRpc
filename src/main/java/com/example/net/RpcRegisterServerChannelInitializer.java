package com.example.net;



import com.example.context.RpcServiceContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.DefaultEventLoopGroup;

import java.util.List;

/**
 * 注册中心信道处理器
 */
public class RpcRegisterServerChannelInitializer extends RpcServerChannelInitializer {









    /**
     * 初始化信道
     * @param handlersChain handler链
     * @param workerGroup 工作线程池
     * @param heartCheckTime 心跳检测间隔
     */
    public RpcRegisterServerChannelInitializer(List<ChannelHandler> handlersChain) {
        super(handlersChain);
    }



}
