package com.myrpc.Factory;

import com.myrpc.context.NacosProperties;
import com.myrpc.context.RpcProperties;
import com.myrpc.net.client.NacosClient;
import com.myrpc.net.client.RegisterClient;
import com.myrpc.protocol.Enums.RegisterType;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class RegisterClientFactory  {


    RegisterClient client=null;

    RegisterType registerType;
    final RpcProperties rpcProperties;

    Class<? extends  RegisterClient> registerClass;

    public RegisterClientFactory(RpcProperties rpcProperties) throws Exception {
        this.rpcProperties = rpcProperties;
        registerType=rpcProperties.getRegisterProperties().getRegisterType();
        registerClass= RegisterType.ToRegisterClass(registerType);
    }
    /**
     * @return
     * @throws Exception
     */
    @Bean
    public RegisterClient registerClient() throws Exception {
        if (client==null)
        {
            if (registerType.equals(RegisterType.NACOS)) {
                client = new NacosClient(rpcProperties);
            }
            Assert.isTrue(client != null, "no match register type...");
        }
        return client;
    }


}
