package com.example.context;

import com.example.Factory.PropertiesFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
@Slf4j
@Data
@Component
public class RpcProperties {




    RpcRegisterProperties registerProperties;
    RpcNetProperties rpcNetProperties;


    Environment environment;

    AtomicBoolean isInit=new AtomicBoolean(false);

    public RpcRegisterProperties getRegisterProperties() throws Exception {
        if (isInit.get())
            return registerProperties;
        else
            throw new Exception("RpcProperties not init");
    }

    public RpcNetProperties getRpcNetProperties() throws Exception {
        if (isInit.get())
            return rpcNetProperties;
        else
            throw new Exception("RpcProperties not init");
    }

    public boolean isInit() {
        return isInit.get();
    }

    public RpcProperties(Environment environment) {
        this.environment = environment;
    }

    public void init() throws Exception {
        if (isInit.compareAndSet(false,true))
        {
            initRegisterProperties();
            initRpcNetProperties();
        }
        else {
            log.warn("RpcProperties has been init");
        }
    }

    public void initRegisterProperties() throws Exception {
        registerProperties = PropertiesFactory.getRegisterProperties(environment);
    }

    public void initRpcNetProperties() throws UnknownHostException {
        rpcNetProperties = PropertiesFactory.getRpcNetProperties(environment);
    }










}
