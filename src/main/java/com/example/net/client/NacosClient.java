package com.example.net.client;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.example.net.NacosRPCServiceInstance;
import com.example.net.RPCServiceInstance;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class NacosClient implements RegisterClient {





    private  NamingService registerService;



    /**
     * 初始化
     */
    @Override
    public void init(String serverAddr) throws NacosException {
        log.info("Nacos Client[{}] init...",serverAddr);
       // registerService= NamingFactory.createNamingService(Properties);
    }

    public void init(Properties properties) throws NacosException {
        registerService=NamingFactory.createNamingService(properties);
        init(properties.getProperty("serverAddr"));
    }

    /**
     * @param serviceInstance
     */
    @Override
    public void register(RPCServiceInstance serviceInstance) throws Exception {
        if (serviceInstance instanceof Instance)
            registerService.registerInstance(serviceInstance.getServiceName(), (Instance) serviceInstance);
        else
            throw new Exception("RPCServiceInstance is not instanceof Instance");
    }

    /**
     * @param serviceInstance
     */
    @Override
    public void deregister(RPCServiceInstance serviceInstance) throws Exception {
        if (serviceInstance instanceof Instance)
            registerService.deregisterInstance(serviceInstance.getServiceName(), (Instance) serviceInstance);
        else
            throw new Exception("RPCServiceInstance is not instanceof Instance");
    }

    /**
     * @return
     */
    @Override
    public List<RPCServiceInstance> getAllInstances(String serviceName) throws NacosException {

        LinkedList<RPCServiceInstance> instances = new LinkedList<>();
        registerService.getAllInstances(serviceName).forEach(instance -> {
            if (instance instanceof RPCServiceInstance) {
                instances.add((RPCServiceInstance) instance);
            }
        });
        return instances;
    }

    /**
     * @param serviceName
     * @param healthy
     * @return
     */
    @Override
    public List<RPCServiceInstance> selectInstances(String serviceName, boolean healthy) throws NacosException {
        LinkedList<RPCServiceInstance> instances = new LinkedList<>();
        registerService.selectInstances(serviceName,healthy).forEach(instance -> {
            if (instance instanceof  RPCServiceInstance)
                instances.add((RPCServiceInstance) instance);
        });
        return instances;
    }

    /**
     * @param serviceName
     * @return
     */
    @Override
    public RPCServiceInstance selectOneHealthyInstance(String serviceName) throws NacosException {
        Instance instance = registerService.selectOneHealthyInstance(serviceName);
        return instance instanceof RPCServiceInstance ? (RPCServiceInstance) instance : null;
    }




}
