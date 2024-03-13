package com.myrpc.context;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RpcRunner implements ApplicationRunner {


    RpcServiceContext context;

    public RpcRunner(RpcServiceContext context) {
        this.context = context;
    }

    /**
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        context.registerLocalService();
    }
}
