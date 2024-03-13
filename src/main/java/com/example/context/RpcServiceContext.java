package com.example.context;

import com.example.net.RPCServiceInstance;
import com.example.net.client.RegisterClient;
import com.example.net.client.ServiceClient;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class RpcServiceContext {

    RegisterClient registerClient;

    ServiceClient serviceClient;


    RpcProperties rpcProperties;





    /**
     * key为服务名称,value为实例
     */
    private final ConcurrentHashMap<String, RPCServiceInstance> serviceInstanceMap=new ConcurrentHashMap<>();



    public RPCServiceInstance addServiceInstance(RPCServiceInstance serviceInstance)
    {
        return serviceInstanceMap.put(serviceInstance.getServiceName(), serviceInstance);
    }

    public RPCServiceInstance removeServiceInstance(RPCServiceInstance serviceInstance)
    {
        return serviceInstanceMap.remove(serviceInstance.getServiceName());
    }






}
