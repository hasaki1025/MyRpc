package com.example.context;

import com.example.Factory.RegisterClientFactory;
import com.example.net.RPCServiceInstance;
import com.example.net.client.RegisterClient;
import com.example.net.client.ServiceClient;
import com.example.net.client.ServiceClientPool;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Configuration
@Slf4j
public class RpcServiceContext implements InitializingBean {

    RegisterClient registerClient;

    final ServiceClientPool serviceClientPool;

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

    public final ConcurrentHashMap<String,Object> localServiceObjectMap=new ConcurrentHashMap<>();

    public RpcServiceContext(ServiceClientPool serviceClientPool, RpcProperties rpcProperties) {
        this.serviceClientPool = serviceClientPool;
        this.rpcProperties = rpcProperties;
    }

    public void init()
    {
        if (isInit.compareAndSet(false,true))
        {
            log.info("RPC Context start init....");
            try {
                rpcProperties.init();
                registerClient= RegisterClientFactory.createClient(rpcProperties);
                registerClient.init(rpcProperties.getRegisterProperties().getIp(), rpcProperties.getRegisterProperties().getPort());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        else {
            log.warn("context has been init...");
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


    public void addRemoteService(Class<?> interfaceClass,String serviceName)
    {
        serviceName=serviceName.isEmpty() ? interfaceClass.getCanonicalName() : serviceName;
        RPCServiceInstance instance = remoteServiceMap.computeIfAbsent(serviceName, (name) -> {
            try {
                return registerClient.selectOneHealthyInstance(name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        addServiceInterface(interfaceClass,serviceName);
    }


    public Object getLocalServiceObject(String serviceName)
    {
        return localServiceObjectMap.get(serviceName);
    }


    public RPCServiceInstance addLocalService(RPCServiceInstance serviceInstance,Object bean)
    {
        localServiceObjectMap.put(serviceInstance.getServiceName(),bean);
        return localServiceMap.put(serviceInstance.getServiceName(), serviceInstance);
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


    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    public ServiceClient getServiceClient(String serviceName) {
        RPCServiceInstance instance = remoteServiceMap.get(serviceName);
        //TODO 负载均衡实现？
        return serviceClientPool.getConnection(instance.getIp() + ":" + instance.getPort());
    }


    public void returnBackConnection(ServiceClient serviceClient)
    {
        serviceClientPool.returnConnection(serviceClient);
    }
}
