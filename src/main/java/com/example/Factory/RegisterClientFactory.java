package com.example.Factory;

import com.example.context.RpcProperties;
import com.example.net.client.NacosClient;
import com.example.net.client.RegisterClient;
import com.example.protocol.Enums.RegisterType;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


public class RegisterClientFactory {


    public static RegisterClient createClient(RpcProperties rpcProperties) throws Exception {

        RegisterClient client = null;
        RegisterType registerType = rpcProperties.getRegisterProperties().getRegisterType();
        if (registerType.equals(RegisterType.NACOS)) {
            client = new NacosClient(rpcProperties);
        }
        Assert.isTrue(client != null, "no match register type...");
        return client;
    }


}
