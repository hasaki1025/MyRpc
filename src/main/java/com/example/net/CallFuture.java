package com.example.net;

import com.example.protocol.content.ResponseContent;

import io.netty.channel.DefaultEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class CallFuture implements Callable<ResponseContent>{


    private ResponseContent content;
    private final CountDownLatch latch=new CountDownLatch(1);

    public void setFail()
    {
        ResponseContent failContent = new ResponseContent();
        failContent.setHasException(true);
        failContent.setResult("call time out");
        setResponse(failContent);
    }


    public void setResponse(ResponseContent content)
    {
        this.content=content;
        latch.countDown();
    }

    @Override
    public ResponseContent call() throws Exception {
        log.debug("waiting for response...");
        latch.await();
        log.debug("get Response...");
        return content;
    }
}
