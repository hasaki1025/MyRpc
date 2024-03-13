package com.myrpc.context;

import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.myrpc.Factory.ServiceInstanceFactory;
import com.myrpc.net.RPCServiceInstance;
import com.myrpc.net.client.RegisterClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Configuration
@Slf4j
public class RpcServiceContext  {

    final RegisterClient registerClient;
    final RpcProperties rpcProperties;


    AtomicBoolean isInit=new AtomicBoolean(false);

    /**
     * 用于保存本地提供服务的服务实例
     */
    private final ConcurrentHashMap<String, RPCServiceInstance> localServiceMap=new ConcurrentHashMap<>();

    /**
     * 用于接口Class与其对应服务名称的映射关系
     */
    public final ConcurrentHashMap<Class<?>,String> serviceInterfaceMap=new ConcurrentHashMap<>();

    public final ConcurrentHashMap<String,Object> localServiceObject=new ConcurrentHashMap<>();

    public final CopyOnWriteArraySet<String> subscribeServiceSet=new CopyOnWriteArraySet<>();


    public RpcServiceContext(RegisterClient registerClient, RpcProperties rpcProperties) {
        this.registerClient = registerClient;
        this.rpcProperties = rpcProperties;
    }





    public void addLocalService(RPCServiceInstance serviceInstance,Object bean) throws Exception {
        localServiceMap.put(serviceInstance.getServiceName(),serviceInstance);
        localServiceObject.put(serviceInstance.getServiceName(),bean);
    }

    public void init()
    {
        if (isInit.compareAndSet(false,true))
        {
            log.info("RPC Context start init....");
            registerLocalService();
            subscribeRemoteService();
        }
        else {
            log.warn("context has been init...");
        }
    }


    void subscribeRemoteService()
    {
        log.info("send Subscribe Request To Register");
        subscribeServiceSet.forEach(s -> {
            try {
                registerClient.subscribeService(s);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }



     void registerLocalService()
    {
        log.info("start register local service");
        if (isInit.get())
        {
            localServiceMap.values().forEach(rpcServiceInstance -> {
                try {
                    registerClient.register(rpcServiceInstance);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void addSubscribeService(String serviceName)
    {
        subscribeServiceSet.add(serviceName);
    }


    public RPCServiceInstance getServiceInstanceByInterfaceClass(Class<?> interfaceClass) throws Exception {
        if (isInit.get())
        {
            String serviceName = serviceInterfaceMap.get(interfaceClass);
            Assert.isTrue(serviceName!=null,"not service match this interface");
            return registerClient.selectOneHealthyInstance(serviceName);
        }
        throw new RuntimeException("no init context");
    }




    public Object getLocalServiceObject(String serviceName)
    {
        if (isInit.get())
        {
            return localServiceObject.get(serviceName);
        }
        throw new RuntimeException("no init context");
    }



    public String addServiceInterface(Class<?> interfaceClass,String serviceName)
    {
        return serviceInterfaceMap.put(interfaceClass, serviceName);
    }






}
