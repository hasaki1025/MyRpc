package com.myrpc.net;


public interface RPCServiceInstance {



     String getServiceName();
     String getIp();
     int getPort();

     boolean enableSSL();


     double getWeight();






}
