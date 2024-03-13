package com.myrpc.Factory;

import com.myrpc.context.RpcProperties;
import com.myrpc.net.AESConvertor;
import com.myrpc.net.DefaultConvertor;
import com.myrpc.net.EncipherConvertor;
import com.myrpc.net.RSAConvertor;
import com.myrpc.protocol.Enums.EncryptionMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EncryptionFactory  {



    EncryptionMethod method;
    Class<? extends EncipherConvertor> methodClass;

    RpcProperties rpcProperties;

    public EncryptionFactory(RpcProperties rpcProperties) throws Exception {
        this.rpcProperties = rpcProperties;
        method=rpcProperties.getRpcNetProperties().getProtocolProperties().getEncryptionMethod();
        methodClass=EncryptionMethod.toEncryptionMethodClass(method);
    }
    /**
     * @return
     * @throws Exception
     */
    @Bean
    public EncipherConvertor encipherConvertor() throws Exception {
        if (method.equals(EncryptionMethod.DEFAULT))
            return new DefaultConvertor();
        else if (method.equals(EncryptionMethod.RSA)) {
            return new RSAConvertor();
        } else if (method.equals(EncryptionMethod.AES)) {
            return new AESConvertor(rpcProperties.getRpcNetProperties().getProtocolProperties().getAES_SecretKey());
        }
        throw new Exception("no match EncryptionMethod");
    }

}
