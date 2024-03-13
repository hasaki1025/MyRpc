package com.myrpc.context;

import com.myrpc.Annotation.RpcService;
import com.myrpc.Factory.RegisterClientFactory;
import com.myrpc.Factory.ServiceInstanceFactory;
import com.myrpc.net.ProviderServer;
import com.myrpc.net.RPCServiceInstance;
import com.myrpc.net.client.RegisterClient;
import com.myrpc.net.client.ServiceClient;
import com.myrpc.net.client.ServiceClientPool;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Configuration
@Slf4j
public class RpcServiceContext {

    final RegisterClient registerClient;

    final ServiceClientPool serviceClientPool;

    final RpcProperties rpcProperties;

    final ProviderServer server;

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


    public RpcServiceContext(RegisterClient registerClient, ServiceClientPool serviceClientPool, RpcProperties rpcProperties, ProviderServer server) {
        this.registerClient = registerClient;
        this.serviceClientPool = serviceClientPool;
        this.rpcProperties = rpcProperties;
        this.server = server;
    }


    public void addLocalService(Object bean)
    {
        String serviceName;

        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (!rpcService.serviceName().isEmpty())
        {
            serviceName= rpcService.serviceName();
        }
        else if (!rpcService.interfaceClass().equals(void.class)){
               serviceName=rpcService.interfaceClass().getCanonicalName();
        }
        else {
            Class<?>[] interfaces = beanClass.getInterfaces();
            Assert.isTrue(interfaces.length>0,"illegal RpcService");
            serviceName= interfaces[0].getCanonicalName();
        }
        localServiceObject.put(serviceName,bean);
    }

    public void init()
    {
        if (isInit.compareAndSet(false,true))
        {
            log.info("RPC Context start init....");
            try {
                rpcProperties.init();
                registerClient.init(rpcProperties.getRegisterProperties().getIp(), rpcProperties.getRegisterProperties().getPort());
                server.init();
                rpcProperties.getRpcNetProperties().setPort(server.getPort());
                initLocalService();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        else {
            log.warn("context has been init...");
        }
    }


    /**
     * 初始化本地服务实例并注册
     */
    private void initLocalService()
    {
        if (isInit.get())
        {
            localServiceObject.values().forEach(object -> {
                try {
                    RPCServiceInstance instance = ServiceInstanceFactory.getServiceInstance(rpcProperties, object.getClass());
                    localServiceMap.put(instance.getServiceName(),instance);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            registerLocalService();
        }
    }

    private void registerLocalService()
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
        serviceName=serviceName.isEmpty() ? interfaceClass.getCanonicalName() : serviceName;
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



    public ServiceClient getServiceClient(String serviceName) {
        RPCServiceInstance instance = remoteServiceMap.computeIfAbsent(serviceName,name->{
            try {
                return registerClient.selectOneHealthyInstance(name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        //TODO 负载均衡实现？
        return serviceClientPool.getConnection(instance.getIp() + ":" + instance.getPort());
    }


    public void returnBackConnection(ServiceClient serviceClient)
    {
        serviceClientPool.returnConnection(serviceClient);
    }


}
