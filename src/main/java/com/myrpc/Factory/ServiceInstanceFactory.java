package com.myrpc.Factory;

import com.myrpc.Annotation.RpcService;
import com.myrpc.context.RpcProperties;
import com.myrpc.net.NacosRPCServiceInstance;
import com.myrpc.net.RPCServiceInstance;
import com.myrpc.protocol.Enums.RegisterType;
import org.springframework.util.Assert;

public class ServiceInstanceFactory {




    public static RPCServiceInstance getServiceInstance(RpcProperties properties, Class<?> serviceClass) throws Exception {
        RpcService annotation = serviceClass.getAnnotation(RpcService.class);
        if (properties.getRegisterProperties().getRegisterType().equals(RegisterType.NACOS)) {
            NacosRPCServiceInstance instance = new NacosRPCServiceInstance();
            if (!annotation.instanceId().isEmpty())
                instance.setInstanceId(annotation.instanceId());
            if (!annotation.serviceName().isEmpty())
                instance.setServiceName(annotation.serviceName());
            else if (!annotation.interfaceClass().equals(void.class))
                instance.setServiceName(annotation.interfaceClass().getCanonicalName());
            else {
                Class<?>[] interfaces = serviceClass.getInterfaces();
                Assert.isTrue(interfaces.length>0,"illegal service");
                instance.setServiceName(interfaces[0].getCanonicalName());
            }
            instance.setEnabled(annotation.enabled());
            instance.setEphemeral(annotation.ephemeral());
            instance.setHealthy(annotation.healthy());
            String clusterName = annotation.clusterName();
            if (!clusterName.isEmpty())
                instance.setClusterName(clusterName);
            instance.setIp(properties.getRpcNetProperties().getIp());
            instance.setPort(properties.getRpcNetProperties().getPort());
           //TODO 注解中不支持配置Map类型，MetaData属性暂时不可用
            // instance.setMetadata();
            return instance;
        }
        throw new Exception("not match type Instance");
    }

}
