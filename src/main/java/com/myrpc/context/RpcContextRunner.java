package com.myrpc.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RpcContextRunner implements ApplicationRunner {


    RpcServiceContext context;

    public RpcContextRunner(RpcServiceContext context) {
        this.context = context;
    }

    /**
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("start init context...");
        context.init();
    }
}
