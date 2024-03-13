package com.myrpc.net;

import com.myrpc.protocol.RPCResponse;
import com.myrpc.protocol.content.ResponseContent;
import io.netty.channel.EventLoopGroup;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 每个信道绑定一个ResponseMap用于生成请求的ID以及同步调用和异步调用的实现,
 * 当请求发出时将CallFuture放入Map，当接收到响应时需要设置响应(响应数据保存在Future中)，当获取到响应时需要将请求从Map中移除
 */

public class ResponseMap {




    public final AtomicInteger requestIDCounter=new AtomicInteger(0);


    private final ConcurrentHashMap<Integer,CallFuture> waitingMap=new ConcurrentHashMap<>();

    long timeout;


    public ResponseMap(long timeout) {
        this.timeout = timeout;
    }



    /**
     * 从Map中移除该等待请求并设置对应响应
     * @param response
     */
    public void setResponse(RPCResponse response)
    {
        CallFuture future = waitingMap.remove(response.getSeq());
        future.getCallTask().setContent(response.getContent());
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
    public CallFuture addWaitingRequest(int seq, EventLoopGroup executors)
    {
        CallFuture future = CompletableFutureFactory.commitCallTask(timeout, executors);
        waitingMap.put(seq, future);
        return future;
    }



    /**
     * @param seq 请求序号
     * @return 该请求是否已获得响应（或者说已经超时）
     */
    public boolean stillWaiting(int seq) {
        Future<ResponseContent> future = waitingMap.get(seq);
        return future!=null && !future.isDone();
    }
}
