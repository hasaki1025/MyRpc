package com.myrpc.net;

import com.myrpc.protocol.content.ResponseContent;
import lombok.Data;

import java.util.concurrent.*;
@Data
public class CallFuture implements Future<ResponseContent>{

    CallTask callTask;

    CompletableFuture<ResponseContent> future;

    public CallFuture(CallTask callTask, CompletableFuture<ResponseContent> future) {
        this.callTask = callTask;
        this.future = future;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public ResponseContent get() throws InterruptedException, ExecutionException {
        return future.get();
    }

    @Override
    public ResponseContent get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout,unit);
    }
}
