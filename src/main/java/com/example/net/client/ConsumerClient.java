package com.example.net.client;

import com.example.protocol.RPCRequest;
import com.example.protocol.content.ResponseContent;

import java.util.concurrent.Future;

public interface ConsumerClient extends  Client{

    public Future<ResponseContent> call(RPCRequest request) throws Exception;

}
