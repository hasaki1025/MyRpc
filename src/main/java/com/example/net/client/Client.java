package com.example.net.client;

import io.netty.channel.ChannelFutureListener;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Client implements Closeable {


    AtomicBoolean isInit = new AtomicBoolean(false);



    public abstract void init(String ip, int port);



    public boolean isInit() {
        return isInit.get();
    }




    /**
     * 关闭当前连接
     *
     * @throws IOException 连接关闭抛出
     */
    @Override
    public void close() throws IOException {
        //NOOP
    }

}
