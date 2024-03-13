package com.myrpc.Factory;

import com.myrpc.Util.IPUtil;
import com.myrpc.context.*;

import com.myrpc.protocol.Enums.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.Properties;
@Slf4j
public class PropertiesFactory {

    public static final String REGISTER_ADDR_KEY="MyRpc.register.address";

    public static final String REGISTER_AUTH_ENABLE_KEY="MyRpc.register.auth.enabled";


    public static final String REGISTER_AUTH_USERNAME_KEY="MyRpc.register.auth.userName";
    public static final String REGISTER_AUTH_PASSWORD_KEY="MyRpc.register.auth.password";


    public static final String CHANNEL_TYPE_KEY="MyRpc.net.ChannelType";

    public  static final String REQUEST_TIME_OUT_KEY="MyRpc.net.RequestTimeOut";
    public static final String  LOCAL_HOST="MyRpc.net.localHost";

    public static final String ENCRYPTIONMETHOD_KEY="MyRpc.net.protocol.EncryptionMethod.name";
    public static final String SerializableType_key="MyRpc.net.protocol.SerializableType";
    public static final String SERVICE_PORT_KEY="MyRpc.service.port";

    public static final String AES_SECRETKEY_KEY="MyRpc.net.protocol.EncryptionMethod.SecretKey";

    public static final String SSL_ENABLE="MyRpc.net.ssl.enable";
    public static final String SSL_CA_CRT_PATH="MyRpc.net.ssl.CACrtPath";
    public static final String SSL_SERVER_PRIVATE_PATH="MyRpc.net.ssl.PrivateKeyPath";

    public static final String SSL_SERVER_CRT_PATH="MyRpc.net.ssl.ServerCrtPath";

    public static final String LOAD_BALANCE_POLICY="MyRpc.register.loadBalancePolicy";

    public static SSLProperties getSSLProperties(Environment environment, ResourceLoader resourceLoader)
    {
        SSLProperties sslProperties = new SSLProperties();
        String property = environment.getProperty(SSL_ENABLE);
        if (!Boolean.parseBoolean(property))
        {
            sslProperties.setEnable(false);
            return sslProperties;
        }
        sslProperties.setEnable(true);
        sslProperties.setPrivateKeyResource(resourceLoader.getResource(environment.getProperty(SSL_SERVER_PRIVATE_PATH)));
        sslProperties.setCaCrtResource(resourceLoader.getResource(environment.getProperty(SSL_CA_CRT_PATH)));
        sslProperties.setServerCRTResource(resourceLoader.getResource(environment.getProperty(SSL_SERVER_CRT_PATH)));
        return sslProperties;
    }



    public static RpcRegisterProperties getRegisterProperties(Environment environment) throws Exception {
        String addr = environment.getProperty(REGISTER_ADDR_KEY);
        if (addr==null)
            throw new Exception("no regiser address");
        RpcRegisterProperties rpcRegisterProperties=null;
        if (addr.startsWith(NacosProperties.ADDR_PREFIX))
        {
            rpcRegisterProperties=getNacosProperties(environment);
            String property = environment.getProperty(LOAD_BALANCE_POLICY);
            LoadBalancePolicyType policyType=null;
            if (property==null || property.isEmpty() || property.isBlank())
            {
                policyType=LoadBalancePolicyType.WeightedRandom;
            }
            else {
                policyType  = LoadBalancePolicyType.valueOf(property);
            }
            rpcRegisterProperties.setLoadBalancePolicyType(policyType);
        }
        return rpcRegisterProperties;
    }

    public static NacosProperties getNacosProperties(Environment environment) throws Exception {
        String addr = environment.getProperty(REGISTER_ADDR_KEY);
        if (!addr.startsWith(NacosProperties.ADDR_PREFIX))
        {
            throw new Exception("no nacos type");
        }
        NacosProperties nacosProperties = new NacosProperties();
        nacosProperties.setRegisterType(RegisterType.NACOS);

        Properties properties = new Properties();
        addr=addr.substring(NacosProperties.ADDR_PREFIX.length());

        properties.setProperty(NacosProperties.SERVER_ADDR_KEY,addr);
        nacosProperties.setAddress(addr);


        String[] split = addr.split(":");
        nacosProperties.setIp(split[0]);
        nacosProperties.setPort(Integer.parseInt(split[1]));
        boolean authEnabled = Boolean.parseBoolean(environment.getProperty(REGISTER_AUTH_ENABLE_KEY));
        nacosProperties.setAuthEnabled(authEnabled);
        if (authEnabled)
        {
            String userName = environment.getProperty(REGISTER_AUTH_USERNAME_KEY);
            String password = environment.getProperty(REGISTER_AUTH_PASSWORD_KEY);
            nacosProperties.setUserName(userName);
            nacosProperties.setPassword(password);
            properties.setProperty(NacosProperties.AUTH_USERNAME_KEY,userName);
            properties.setProperty(NacosProperties.AUTH_PASSWORD_KEY,password);
        }
        nacosProperties.setProperties(properties);
        return nacosProperties;
    }


    public static RpcNetProperties getRpcNetProperties(Environment environment,ResourceLoader resourceLoader) throws UnknownHostException {
        RpcNetProperties netProperties = new RpcNetProperties();
        try {
            netProperties.setChannelType(ChannelType.valueOf(environment.getProperty(CHANNEL_TYPE_KEY)));
        }catch (IllegalArgumentException e)
        {
            log.warn("no match channelType");
            netProperties.setChannelType(ChannelType.NIO);
        }

        try {
            netProperties.setRequestTimeOut(Long.parseLong(environment.getProperty(REQUEST_TIME_OUT_KEY)));
        }
        catch (IllegalArgumentException e)
        {
            netProperties.setRequestTimeOut(1000);
        }

        String ip = environment.getProperty(LOCAL_HOST);
        if (!IPUtil.isIPAddress(ip))
            ip= IPUtil.getLocalIPAddress();


        int port = Integer.parseInt(environment.getProperty(SERVICE_PORT_KEY));
        netProperties.setPort(port);
        netProperties.setIp(ip);
        netProperties.setProtocolProperties(getProtocolProperties(environment));

        netProperties.setSslProperties(getSSLProperties(environment,resourceLoader));
        return netProperties;
    }

    static ProtocolProperties getProtocolProperties(Environment environment)
    {
        ProtocolProperties protocolProperties = new ProtocolProperties();
        try {
            protocolProperties.setEncryptionMethod(EncryptionMethod.valueOf(environment.getProperty(ENCRYPTIONMETHOD_KEY)));
        }catch (IllegalArgumentException e)
        {
            log.warn("no match EncryptionMethod,use default");
            protocolProperties.setEncryptionMethod(EncryptionMethod.DEFAULT);
        }
        if (protocolProperties.getEncryptionMethod().equals(EncryptionMethod.AES))
            protocolProperties.setAES_SecretKey(environment.getProperty(AES_SECRETKEY_KEY));
        try {
            protocolProperties.setSerializableType(SerializableType.valueOf(environment.getProperty(SerializableType_key)));
        }catch (IllegalArgumentException e)
        {
            protocolProperties.setSerializableType(SerializableType.MESSAGE_PACK);
        }
        return protocolProperties;
    }
}
