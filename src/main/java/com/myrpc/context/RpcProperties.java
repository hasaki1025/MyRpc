package com.myrpc.context;

import com.myrpc.Factory.PropertiesFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
@Slf4j
@Component
public class RpcProperties  {




    RpcRegisterProperties registerProperties;
    RpcNetProperties rpcNetProperties;


    Environment environment;

    ResourceLoader resourceLoader;

    AtomicBoolean isInit=new AtomicBoolean(false);

    public RpcProperties(Environment environment,ResourceLoader resourceLoader) throws Exception {
        this.environment = environment;
        this.resourceLoader=resourceLoader;
        init();
    }


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
        rpcNetProperties = PropertiesFactory.getRpcNetProperties(environment,resourceLoader);
    }



}
