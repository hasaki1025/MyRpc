package com.myrpc.Factory;

import com.myrpc.context.NacosProperties;
import com.myrpc.context.RpcProperties;
import com.myrpc.net.client.NacosClient;
import com.myrpc.net.client.RegisterClient;
import com.myrpc.protocol.Enums.RegisterType;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class RegisterClientFactory implements FactoryBean<RegisterClient> {







    RegisterClient client=null;

    RegisterType registerType;
    final RpcProperties rpcProperties;

    Class<? extends  RegisterClient> registerClass;

    public RegisterClientFactory(@Value("${MyRpc.register.address}") String registerAddress, RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
        if (registerAddress.startsWith(NacosProperties.ADDR_PREFIX))
        {
            registerType=RegisterType.NACOS;
            registerClass= NacosClient.class;
        }
    }
    /**
     * @return
     * @throws Exception
     */
    @Override
    public RegisterClient getObject() throws Exception {
        if (client==null)
        {
            if (registerType.equals(RegisterType.NACOS)) {
                client = new NacosClient(rpcProperties);
            }
            Assert.isTrue(client != null, "no match register type...");
        }
        return client;
    }

    /**
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return RegisterClient.class;
    }

    /**
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
