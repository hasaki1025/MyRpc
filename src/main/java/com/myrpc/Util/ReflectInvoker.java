package com.myrpc.Util;

import com.myrpc.protocol.content.RequestContent;
import com.myrpc.protocol.content.ResponseContent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectInvoker {


    public static ResponseContent invoke(Object target, RequestContent content)  {


        Method method = null;

        ResponseContent responseContent = new ResponseContent();
        try {
            Object[] args = content.getArgs();
            Class<?>[] classes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                classes[i]=args[i].getClass();
            }
            method = target.getClass().getMethod(content.getMethodName(), classes);
            Object result = method.invoke(target, args);
            responseContent.setResult(result);
            responseContent.setSuccessful(true);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            responseContent.setSuccessful(false);
            responseContent.setResult(e.getMessage());
        }

        return responseContent;
    }
}
