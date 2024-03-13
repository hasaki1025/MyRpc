package com.example.net;

import com.example.protocol.RPCRequest;
import com.example.protocol.content.ResponseContent;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.Future;

import java.nio.channels.Channel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 每个信道绑定一个ResponseMap用于生成请求的ID以及同步调用和异步调用的实现
 */

public class ResponseMap {

    public static final String CHANNEL_RESPONSE_MAP="ChannelResponseMap";


    public final AtomicInteger requestIDCounter=new AtomicInteger(0);

    private final ConcurrentHashMap<Integer, CallFuture> waitingMap=new ConcurrentHashMap<>();


    private final DefaultEventLoopGroup eventLoopGroup;

    long timeout;

    TimeUnit unit=TimeUnit.MILLISECONDS;

    public ResponseMap(DefaultEventLoopGroup eventLoopGroup, long timeout, TimeUnit unit) {
        this.eventLoopGroup = eventLoopGroup;
        this.timeout = timeout;
        this.unit = unit;
    }

    public ResponseMap(DefaultEventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }


    public int getNextRequestID()
    {
        return requestIDCounter.getAndIncrement();
    }

    public Future<ResponseContent> addWaitingRequest(int seq)
    {
        CallFuture callFuture = new CallFuture();
        waitingMap.put(seq, callFuture);
        eventLoopGroup.schedule(()->{
            if (waitingMap.containsKey(seq))
            {
                CallFuture future = waitingMap.get(seq);
                future.setFail();
            }

        },timeout,unit);
        return eventLoopGroup.submit(callFuture);
    }

    public CallFuture remove(int seq)
    {
        return waitingMap.remove(seq);
    }

    public CallFuture getAndRemove(int seq)
    {
        return waitingMap.remove(seq);
    }


    public boolean stillWaiting(int seq) {
        return waitingMap.containsKey(seq);
    }
}
