package com.example.net.client;

import io.netty.channel.ChannelFutureListener;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public interface Client extends Closeable {






    public void init(String ip, int port) throws Exception;



    public boolean isInit();




    /**
     * 关闭当前连接
     *
     * @throws IOException 连接关闭抛出
     */
    @Override
    public void close() throws IOException ;

}
