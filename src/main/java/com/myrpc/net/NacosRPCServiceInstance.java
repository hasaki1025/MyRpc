package com.myrpc.net;

import com.alibaba.nacos.api.naming.pojo.Instance;


public class NacosRPCServiceInstance extends  Instance implements RPCServiceInstance {


    public NacosRPCServiceInstance() {
        super();
    }

    public NacosRPCServiceInstance(Instance instance) {
        super();
        this.setServiceName(instance.getServiceName());
        this.setInstanceId(instance.getInstanceId());
        this.setEnabled(instance.isEnabled());
        this.setEphemeral(instance.isEphemeral());
        this.setIp(instance.getIp());
        this.setClusterName(instance.getClusterName());
        this.setHealthy(instance.isHealthy());
        this.setMetadata(instance.getMetadata());
        this.setPort(instance.getPort());
        this.setWeight(instance.getWeight());
    }
}
