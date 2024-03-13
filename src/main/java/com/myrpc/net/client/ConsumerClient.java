package com.myrpc.net.client;

import com.myrpc.protocol.RPCRequest;
import com.myrpc.protocol.content.ResponseContent;

import java.util.concurrent.Future;

public interface ConsumerClient extends  Client{

    public Future<ResponseContent> call(RPCRequest request) throws Exception;

}
