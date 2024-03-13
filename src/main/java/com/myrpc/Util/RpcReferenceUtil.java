package com.myrpc.Util;

import com.myrpc.Annotation.RpcReference;

public class RpcReferenceUtil {

    public static String getServiceName(RpcReference rpcReference,Class<?> fieldClass)
    {
        String serviceName=rpcReference.serviceName();
        if (serviceName.isEmpty()  || serviceName.isBlank())
        {
            serviceName= fieldClass.getCanonicalName();
        }
        return serviceName;
    }

}
