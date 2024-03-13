package com.myrpc.Factory;

import com.myrpc.net.AESConvertor;
import com.myrpc.net.DefaultConvertor;
import com.myrpc.net.EncipherConvertor;
import com.myrpc.net.RSAConvertor;
import com.myrpc.protocol.Enums.EncryptionMethod;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionFactory implements FactoryBean<EncipherConvertor> {

    String encryptionMethod;

    EncryptionMethod method;
    Class<? extends EncipherConvertor> methodClass;



    public EncryptionFactory(@Value("${MyRpc.net.protocol.EncryptionMethod}") String encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
        method=EncryptionMethod.valueOf(encryptionMethod);
        methodClass=EncryptionMethod.toEncryptionMethodClass(method);
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public EncipherConvertor getObject() throws Exception {
        if (method.equals(EncryptionMethod.DEFAULT))
            return new DefaultConvertor();
        else if (method.equals(EncryptionMethod.RSA)) {
            return new RSAConvertor();
        } else if (method.equals(EncryptionMethod.AES)) {
            return new AESConvertor();
        }
        throw new Exception("no match EncryptionMethod");
    }

    /**
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return methodClass;
    }

    /**
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
