package com.myrpc.context;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class SSLProperties {


    Resource serverCRTResource;
    Resource privateKeyResource;
    Resource caCrtResource;
    boolean enable;




}
