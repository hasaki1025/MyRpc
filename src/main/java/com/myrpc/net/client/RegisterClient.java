package com.myrpc.net.client;

import com.myrpc.net.RPCServiceInstance;

import java.util.List;

public interface RegisterClient extends Client {




    void register(RPCServiceInstance instance) throws Exception;

    void deregister(RPCServiceInstance instance) throws Exception;


    List<RPCServiceInstance> getAllInstances(String serviceName) throws Exception;


    List<RPCServiceInstance> selectInstances(String serviceName, boolean healthy) throws Exception;


    RPCServiceInstance selectOneHealthyInstance(String serviceName) throws Exception;


}
