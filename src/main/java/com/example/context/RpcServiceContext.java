package com.example.context;

import com.example.net.RPCServiceInstance;
import com.example.net.client.RegisterClient;
import com.example.net.client.ServiceClient;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class RpcServiceContext {

    RegisterClient registerClient;

    ServiceClient serviceClient;


    RpcProperties rpcProperties;





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












}
