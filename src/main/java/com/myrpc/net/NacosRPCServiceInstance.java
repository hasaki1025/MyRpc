package com.myrpc.net;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


public class NacosRPCServiceInstance extends  Instance implements RPCServiceInstance {

    private final static String split_string="@@";
    private final static String enableSSL_Meta_key="enableSSL";

    boolean enableSSL=false;

    double currentWeight=0;


    public boolean isEnableSSL() {
        return enableSSL;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public void setEnableSSL(boolean enableSSL) {
        this.enableSSL = enableSSL;
        Map<String, String> metadata = this.getMetadata();
        if (metadata==null)
        {
            metadata=new HashMap<>();
        }
        metadata.put(enableSSL_Meta_key, String.valueOf(enableSSL));
    }

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
        this.enableSSL= Boolean.parseBoolean(instance.getMetadata().get(enableSSL_Meta_key));
    }

    /**
     * @return
     */
    @Override
    public boolean enableSSL() {
        return enableSSL;
    }
}
