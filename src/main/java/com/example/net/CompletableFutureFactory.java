package com.example.net;

import com.example.protocol.content.ResponseContent;

import io.netty.channel.DefaultEventLoopGroup;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class CompletableFutureFactory  {

    public static CompletableFuture<ResponseContent> commitCallTask(long timeout)
    {
        return CompletableFuture.supplyAsync(new CallTask()).orTimeout(timeout, TimeUnit.MILLISECONDS);
    }


    public static CompletableFuture<ResponseContent> commitCallTask(long timeout,Executor executor)
    {
        return CompletableFuture.supplyAsync(new CallTask(),executor).orTimeout(timeout, TimeUnit.MILLISECONDS);
    }

}
