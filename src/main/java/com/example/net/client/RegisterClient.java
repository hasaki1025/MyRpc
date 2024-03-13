package com.example.net.client;

import com.alibaba.nacos.api.exception.NacosException;
import com.example.net.RPCServiceInstance;

import java.util.List;

public interface RegisterClient extends Client {




    void register(RPCServiceInstance instance) throws Exception;

    void deregister(RPCServiceInstance instance) throws Exception;


    List<RPCServiceInstance> getAllInstances(String serviceName) throws Exception;


    List<RPCServiceInstance> selectInstances(String serviceName, boolean healthy) throws Exception;


    RPCServiceInstance selectOneHealthyInstance(String serviceName) throws Exception;


}
