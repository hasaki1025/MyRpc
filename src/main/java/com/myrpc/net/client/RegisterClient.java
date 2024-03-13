package com.myrpc.net.client;

import com.alibaba.nacos.api.exception.NacosException;
import com.myrpc.net.RPCServiceInstance;

import java.util.List;

public interface RegisterClient extends Client {




    void register(RPCServiceInstance instance) throws Exception;

    void deregister(RPCServiceInstance instance) throws Exception;

    RPCServiceInstance selectOneHealthyInstance(String serviceName) throws Exception;

    List<RPCServiceInstance> selectHealthyInstance(String serviceName) throws Exception;

    void subscribeService(String serviceName) throws Exception;
}
