package com.myrpc.net.LoadBalance;

import com.myrpc.net.RPCServiceInstance;

import java.util.List;

public interface LoadBalancePolicy {



    RPCServiceInstance selectServiceInstance(String serviceName);
}
