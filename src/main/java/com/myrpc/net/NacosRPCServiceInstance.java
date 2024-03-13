package com.myrpc.net;

import com.alibaba.nacos.api.naming.pojo.Instance;


public class NacosRPCServiceInstance extends  Instance implements RPCServiceInstance {

    private final static String split_string="@@";

    public NacosRPCServiceInstance() {
        super();
    }

    public NacosRPCServiceInstance(Instance instance) {
        super();
        String serviceName = instance.getServiceName();
        String[] split = serviceName.split(split_string);
        serviceName= split[1];
        this.setServiceName(serviceName);
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
