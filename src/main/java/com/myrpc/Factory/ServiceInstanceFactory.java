package com.myrpc.Factory;

import com.myrpc.Annotation.RpcService;
import com.myrpc.context.RpcProperties;
import com.myrpc.net.NacosRPCServiceInstance;
import com.myrpc.net.RPCServiceInstance;
import com.myrpc.protocol.Enums.RegisterType;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
@Component
//@DependsOn({"com.myrpc.net.ProviderServer"})
public class ServiceInstanceFactory {




    RpcProperties properties;

    public ServiceInstanceFactory(RpcProperties properties) {
        this.properties = properties;
    }

    public RPCServiceInstance getServiceInstance(Class<?> serviceClass) throws Exception {
        RpcService annotation = serviceClass.getAnnotation(RpcService.class);
        if (properties.getRegisterProperties().getRegisterType().equals(RegisterType.NACOS)) {
            NacosRPCServiceInstance instance = new NacosRPCServiceInstance();
            if (!annotation.instanceId().isEmpty() && !annotation.instanceId().isBlank())
                instance.setInstanceId(annotation.instanceId());
            if (!annotation.serviceName().isEmpty() && !annotation.serviceName().isBlank())
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
            instance.setEnableSSL(properties.getRpcNetProperties().getSslProperties().isEnable());

            return instance;
        }
        throw new Exception("not match type Instance");
    }

}
