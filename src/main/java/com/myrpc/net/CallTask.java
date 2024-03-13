package com.myrpc.net;

import com.myrpc.protocol.content.ResponseContent;

import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

public class CallTask implements  Supplier<ResponseContent> {


    private final CountDownLatch countDownLatch=new CountDownLatch(1);

    private ResponseContent responseContent;
    public void setContent(ResponseContent content)
    {
        responseContent=content;
        countDownLatch.countDown();
    }

    /**
     * @return
     */
    @Override
    public ResponseContent get() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return responseContent;
    }
}
