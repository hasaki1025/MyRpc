package com.example.Factory;

import com.example.Util.IPUtil;
import com.example.context.NacosProperties;

import com.example.context.ProtocolProperties;
import com.example.context.RpcNetProperties;
import com.example.context.RpcRegisterProperties;
import com.example.protocol.Enums.ChannelType;
import com.example.protocol.Enums.EncryptionMethod;
import com.example.protocol.Enums.RegisterType;
import com.example.protocol.Enums.SerializableType;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class PropertiesFactory {








    public static RpcRegisterProperties getRegisterProperties(Environment environment) throws Exception {
        String addr = environment.getProperty("MyRpc.register.address");
        if (addr==null)
            throw new Exception("no regiser address");
        RpcRegisterProperties rpcRegisterProperties=null;
        if (addr.startsWith(NacosProperties.ADDR_PREFIX))
        {
            rpcRegisterProperties=getNacosProperties(environment);
        }
        return rpcRegisterProperties;
    }

    public static NacosProperties getNacosProperties(Environment environment) throws Exception {
        String addr = environment.getProperty("MyRpc.register.address");
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

        boolean authEnabled = Boolean.getBoolean(environment.getProperty("MyRpc.register.auth.enabled"));

        nacosProperties.setAuthEnabled(authEnabled);
        if (authEnabled)
        {
            String userName = environment.getProperty("MyRpc.register.auth.userName");
            String password = environment.getProperty("MyRpc.register.auth.password");
            nacosProperties.setUserName(userName);
            nacosProperties.setPassword(password);
            properties.setProperty(NacosProperties.AUTH_USERNAME_KEY,userName);
            properties.setProperty(NacosProperties.AUTH_PASSWORD_KEY,password);
        }
        nacosProperties.setProperties(properties);
        return nacosProperties;
    }


    public static RpcNetProperties getRpcNetProperties(Environment environment) throws UnknownHostException {
        RpcNetProperties netProperties = new RpcNetProperties();
        netProperties.setChannelType(ChannelType.valueOf(environment.getProperty("MyRpc.net.ChannelType")));
        netProperties.setRequestTimeOut(Long.parseLong(environment.getProperty("MyRpc.net.RequestTimeOut")));
        String ip = environment.getProperty("MyRpc.net.localHost");
        if (!IPUtil.isIPAddress(ip))
            ip= IPUtil.getLocalIPAddress();

        netProperties.setLocalAddress(ip);
        netProperties.setProtocolProperties(getProtocolProperties(environment));
        return netProperties;
    }

    static ProtocolProperties getProtocolProperties(Environment environment)
    {
        ProtocolProperties protocolProperties = new ProtocolProperties();
        protocolProperties.setEncryptionMethod(EncryptionMethod.valueOf(environment.getProperty("MyRpc.net.protocol.EncryptionMethod")));
        protocolProperties.setSerializableType(SerializableType.valueOf(environment.getProperty("MyRpc.net.protocol.SerializableType")));
        return protocolProperties;
    }
}
