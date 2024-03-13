package com.myrpc.context;

import com.myrpc.protocol.Enums.LoadBalancePolicyType;
import com.myrpc.protocol.Enums.RegisterType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
public class RpcRegisterProperties {

    Properties properties;
    String address;
    String ip;
    int port;
    RegisterType registerType;

    LoadBalancePolicyType loadBalancePolicyType;




}
