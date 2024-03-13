package com.myrpc.context;

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
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Configuration
@Slf4j
public class RpcServiceContext   {

    final RegisterClient registerClient;
    final RpcProperties rpcProperties;


    AtomicBoolean isInit=new AtomicBoolean(false);

    /**
     * 用于保存本地提供服务的服务实例
     */
    private final ConcurrentHashMap<String, RPCServiceInstance> localServiceMap=new ConcurrentHashMap<>();


    /**
     * 用于保存远程服务实例信息Map，TODO 是否需要定期清理
     */
    private final ConcurrentHashMap<String,RPCServiceInstance> remoteServiceMap=new ConcurrentHashMap<>();

    /**
     * 用于接口Class与其对应服务名称的映射关系
     */
    public final ConcurrentHashMap<Class<?>,String> serviceInterfaceMap=new ConcurrentHashMap<>();

    public final ConcurrentHashMap<String,Object> localServiceObject=new ConcurrentHashMap<>();


    public RpcServiceContext(RegisterClient registerClient, RpcProperties rpcProperties) {
        this.registerClient = registerClient;
        this.rpcProperties = rpcProperties;
        init();
    }


    public void addLocalService(RPCServiceInstance serviceInstance,Object bean) throws Exception {
        Class<?> beanClass = bean.getClass();
        localServiceMap.put(serviceInstance.getServiceName(),serviceInstance);
        localServiceObject.put(serviceInstance.getServiceName(),bean);
    }

    public void init()
    {
        if (isInit.compareAndSet(false,true))
        {
            log.info("RPC Context start init....");
        }
        else {
            log.warn("context has been init...");
        }
    }




     void registerLocalService()
    {
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


    public RPCServiceInstance getServiceInstanceByInterfaceClass(Class<?> interfaceClass) throws Exception {
        String serviceName = serviceInterfaceMap.get(interfaceClass);
        Assert.isTrue(serviceName!=null,"not service match this interface");
        return remoteServiceMap.computeIfAbsent(serviceName,key->{
            try {
                return registerClient.selectOneHealthyInstance(key);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    //TODO registerClient尚未初始化
    public void addRemoteService(Class<?> interfaceClass,String serviceName)
    {
        addServiceInterface(interfaceClass,serviceName);
    }


    public Object getLocalServiceObject(String serviceName)
    {
        return localServiceObject.get(serviceName);
    }







    /**默认采用类的规范名称作为服务名称
     * @param interfaceClass
     * @return
     */
    public String addServiceInterface(Class<?> interfaceClass)
    {
        return addServiceInterface(interfaceClass, interfaceClass.getCanonicalName());
    }

    public String addServiceInterface(Class<?> interfaceClass,String serviceName)
    {
        return serviceInterfaceMap.put(interfaceClass, serviceName);
    }



    public RPCServiceInstance getServiceAddress(String serviceName) {
        return remoteServiceMap.computeIfAbsent(serviceName,name->{
            try {
                return registerClient.selectOneHealthyInstance(name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }





}
