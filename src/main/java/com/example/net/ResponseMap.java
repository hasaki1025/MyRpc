package com.example.net;

import com.example.protocol.RPCRequest;
import com.example.protocol.content.ResponseContent;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.Future;

import java.nio.channels.Channel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 每个信道绑定一个ResponseMap用于生成请求的ID以及同步调用和异步调用的实现,
 * 当请求发出时将CallFuture放入Map，当接收到响应时需要设置响应(响应数据保存在Future中)，当获取到响应时需要将请求从Map中移除
 */

public class ResponseMap {

    public static final String CHANNEL_RESPONSE_MAP="ChannelResponseMap";


    public final AtomicInteger requestIDCounter=new AtomicInteger(0);

    private final ConcurrentHashMap<Integer, CompletableFuture<ResponseContent>> waitingMap=new ConcurrentHashMap<>();


    private final DefaultEventLoopGroup eventLoopGroup;

    long timeout;


    public ResponseMap(DefaultEventLoopGroup eventLoopGroup, long timeout) {
        this.eventLoopGroup = eventLoopGroup;
        this.timeout = timeout;

    }

    public ResponseMap(DefaultEventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }


    public int getNextRequestID()
    {
        return requestIDCounter.getAndIncrement();
    }



    /**
     * 新增等待响应的请求并启动超时监听任务,请求失败时Map中将会存储带有异常的响应
     * @param seq
     * @return
     */
    public CompletableFuture<ResponseContent> addWaitingRequest(int seq)
    {
        CompletableFuture<ResponseContent> future = CompletableFutureFactory.commitCallTask(timeout, eventLoopGroup);
        waitingMap.put(seq, future);
        return future;
    }

    public CompletableFuture<ResponseContent> remove(int seq)
    {
        return waitingMap.remove(seq);
    }

    public CompletableFuture<ResponseContent> getAndRemove(int seq)
    {
        return waitingMap.remove(seq);
    }


    public boolean stillWaiting(int seq) {
        return waitingMap.containsKey(seq);
    }
}
