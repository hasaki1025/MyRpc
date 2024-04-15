package com.example.net;

import com.example.protocol.content.ResponseContent;

import io.netty.channel.DefaultEventLoopGroup;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class CompletableFutureFactory  {

    public static CallFuture commitCallTask(long timeout)
    {
        CallTask task = new CallTask();
        CompletableFuture<ResponseContent> future = CompletableFuture.supplyAsync(task).orTimeout(timeout, TimeUnit.MILLISECONDS).exceptionally(
                (exception)->{
                    ResponseContent content = new ResponseContent();
                    content.setSuccessful(false);
                    content.setResult(exception.getMessage());
                    return content;
                }
        );
        return new CallFuture(task, future);
    }


    public static CallFuture commitCallTask(long timeout,Executor executor)
    {
        CallTask task = new CallTask();
        CompletableFuture<ResponseContent> future = CompletableFuture.supplyAsync(task, executor).orTimeout(timeout, TimeUnit.MILLISECONDS).exceptionally(
                (exception)->{
                    ResponseContent content = new ResponseContent();
                    content.setSuccessful(false);
                    content.setResult(exception.getMessage());
                    return content;
                }
        );
        return new CallFuture(task, future);
    }

}
