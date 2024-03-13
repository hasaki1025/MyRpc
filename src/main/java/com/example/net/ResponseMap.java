package com.example.net;

import com.example.protocol.RPCRequest;
import com.example.protocol.content.ResponseContent;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.Future;

import java.nio.channels.Channel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 每个信道绑定一个ResponseMap用于生成请求的ID以及同步调用和异步调用的实现
 */
public class ResponseMap {


    public final AtomicInteger requestIDCounter=new AtomicInteger(0);

    private final ConcurrentHashMap<Integer, CallFuture> waitingMap=new ConcurrentHashMap<>();


    private final DefaultEventLoopGroup eventLoopGroup;

    public ResponseMap(DefaultEventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }


    public int getNextRequestID()
    {
        return requestIDCounter.getAndIncrement();
    }

    public Future<ResponseContent> addWaitingRequest(RPCRequest request)
    {
        CallFuture callFuture = new CallFuture();
        waitingMap.put(request.getSeq(), callFuture);
        return eventLoopGroup.submit(callFuture);
    }


}
