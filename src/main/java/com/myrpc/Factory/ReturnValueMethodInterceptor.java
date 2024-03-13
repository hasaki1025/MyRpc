package com.myrpc.Factory;

import com.myrpc.protocol.content.ResponseContent;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class ReturnValueMethodInterceptor implements MethodInterceptor {


    Future<ResponseContent> callFuture;

    public ReturnValueMethodInterceptor(Future<ResponseContent> callFuture) {
        this.callFuture = callFuture;
    }

    /**
     * @param obj
     * @param method
     * @param args
     * @param proxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        ResponseContent content = callFuture.get();
        return method.invoke(content, args);
    }



}
