package com.example.net.client;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.example.context.NacosProperties;
import com.example.net.NacosRPCServiceInstance;
import com.example.net.RPCServiceInstance;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class NacosClient extends Client implements RegisterClient {





    private  NamingService registerService;





    /**
     * @param ip
     * @param port
     */
    @Override
    public void init(String ip, int port) {
        //NOOP
    }


    public void init(Properties properties) throws Exception {
        if (isInit.compareAndSet(false,true))
        {
            registerService=NamingFactory.createNamingService(properties);
            init(properties.getProperty(NacosProperties.SERVER_ADDR_KEY));
        }
        else
        {
            log.warn("nacos client has been init");
        }
    }

    /**
     * @param address
     * @throws Exception
     */
    @Override
    public void init(String address) throws Exception {
        //NOOP
        log.info("nacos[{}] init...",address);
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
