package com.example.Factory;

import com.example.Annotation.RpcService;
import com.example.protocol.RPCRequest;

import java.lang.reflect.Method;
import java.util.Objects;

public class RpcRequestFactory {


    //TODO 根据调用方法产生RpcRequest
    RPCRequest getRpcRequest(Class<?> interfaceClass, Method method, Object[] args)
    {
        return null;
    }
}
