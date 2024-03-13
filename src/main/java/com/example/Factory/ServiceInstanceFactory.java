package com.example.Factory;

import com.example.Annotation.RpcService;
import com.example.Util.IPUtil;
import com.example.context.RpcProperties;
import com.example.context.RpcRegisterProperties;
import com.example.net.NacosRPCServiceInstance;
import com.example.net.RPCServiceInstance;
import com.example.protocol.Enums.RegisterType;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;

public class ServiceInstanceFactory {


    public static RPCServiceInstance getServiceInstance(RpcProperties properties, RpcService annotation) throws Exception {
        if (properties.getRegisterProperties().getRegisterType().equals(RegisterType.NACOS)) {
            NacosRPCServiceInstance instance = new NacosRPCServiceInstance();
            instance.setInstanceId(annotation.instanceId());
            instance.setServiceName(annotation.serviceName());
            instance.setEnabled(annotation.enabled());
            instance.setEphemeral(annotation.ephemeral());
            instance.setHealthy(annotation.healthy());
            instance.setClusterName(annotation.clusterName());
            instance.setIp(properties.getRpcNetProperties().getLocalAddress());
           //TODO 注解中不支持配置Map类型，MetaData属性暂时不可用
            // instance.setMetadata();
            return instance;
        }
        throw new Exception("not match type Instance");
    }

}
