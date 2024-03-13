package com.myrpc.net.client;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.myrpc.context.NacosProperties;
import com.myrpc.context.RpcProperties;
import com.myrpc.net.LoadBalance.LoadBalancePolicy;
import com.myrpc.net.NacosRPCServiceInstance;
import com.myrpc.net.RPCServiceInstance;
import com.myrpc.protocol.Enums.LoadBalancePolicyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class NacosClient  implements RegisterClient , EventListener {





    private  NamingService registerService;

    final private RpcProperties rpcProperties;


    private final ConcurrentHashMap<String, CopyOnWriteArrayList<NacosRPCServiceInstance>> serviceMap=new ConcurrentHashMap<>();


    private LoadBalancePolicy loadBalancePolicy;

    AtomicBoolean isInit=new AtomicBoolean(false);

    public NacosClient(RpcProperties rpcProperties) throws Exception {
        this.rpcProperties = rpcProperties;
        init(rpcProperties.getRegisterProperties().getProperties());
    }

    /**
     * @param ip
     * @param port
     */
    @Override
    public void init(String ip, int port) throws Exception {
        //NOOP
        init(rpcProperties.getRegisterProperties().getProperties());
    }

    @Override
    public boolean isInit() {
        return isInit.get();
    }

    @Override
    public void close() throws IOException {
        try {
            registerService.shutDown();
        } catch (NacosException e) {
            log.error("{}",e.getErrMsg());
            throw new RuntimeException(e);
        }
    }


    public void init(Properties properties) throws Exception {
        if (isInit.compareAndSet(false,true))
        {
            log.info("nacos[{}] init...",properties.getProperty(NacosProperties.SERVER_ADDR_KEY));
            registerService=NamingFactory.createNamingService(properties);
            LoadBalancePolicyType loadBalancePolicyType = rpcProperties.getRegisterProperties().getLoadBalancePolicyType();
            this.loadBalancePolicy=createLoadBalancePolicy(loadBalancePolicyType);
        }
        else
        {
            log.warn("nacos client has been init");
        }
    }


    private LoadBalancePolicy createLoadBalancePolicy(LoadBalancePolicyType loadBalancePolicyType)
    {
        if (loadBalancePolicyType.equals(LoadBalancePolicyType.WeightedRandom))
        {
            return new WeightedRandomPolicy();
        }
        else if (loadBalancePolicyType.equals(LoadBalancePolicyType.WeightedRoundRobin))
            return new WeightedRoundRobinPolicy();
        throw new RuntimeException("no match type of this LoadBalance Policy");
    }


    /**
     * @param serviceInstance
     */
    @Override
    public void register(RPCServiceInstance serviceInstance) throws Exception {
        if (serviceInstance instanceof Instance)
            registerService.registerInstance(serviceInstance.getServiceName(), (Instance) serviceInstance);
        else
            throw new Exception("RPCServiceInstance is not instanceof Instance");
    }

    /**
     * @param serviceInstance
     */
    @Override
    public void deregister(RPCServiceInstance serviceInstance) throws Exception {
        if (serviceInstance instanceof Instance)
            registerService.deregisterInstance(serviceInstance.getServiceName(), (Instance) serviceInstance);
        else
            throw new Exception("RPCServiceInstance is not instanceof Instance");
    }


    /**
     * 从本地保存的所有服务实例中返回第一个
     * @param serviceName
     * @return
     */
    @Override
    public RPCServiceInstance selectOneHealthyInstance(String serviceName) throws Exception {
        List<NacosRPCServiceInstance> rpcServiceInstances = selectHealthyInstances(serviceName);
        if (rpcServiceInstances.isEmpty())
            throw new RuntimeException("NO service match in register");

        return loadBalancePolicy.selectServiceInstance(serviceName);
    }




    /**
     * 返回多个健康实例
     * @param serviceName
     * @return
     * @throws Exception
     */
    public List<NacosRPCServiceInstance> selectHealthyInstances(String serviceName) throws Exception {
        return serviceMap.computeIfAbsent(serviceName, (name) -> {
            try {
                List<Instance> instances = registerService.selectInstances(name, true);
                CopyOnWriteArrayList<NacosRPCServiceInstance> list = new CopyOnWriteArrayList<>();
                instances.forEach(instance -> {

                    list.add(new NacosRPCServiceInstance(instance));
                });
                return list;
            } catch (NacosException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 订阅服务当服务列表发生变化时更新本地数据集
     * @param serviceName
     */
    @Override
    public void subscribeService(String serviceName) throws NacosException {
        registerService.subscribe(serviceName,this);
    }


    /**
     * 当订阅的服务实例列表发生变化时更新本地服务集
     * @param event event
     */
    @Override
    public void onEvent(Event event) {
        if (event.getClass().equals(NamingEvent.class)) {
            String serviceName = ((NamingEvent) event).getServiceName();
            List<Instance> instances = ((NamingEvent) event).getInstances();
            CopyOnWriteArrayList<NacosRPCServiceInstance> list = new CopyOnWriteArrayList<>();
            instances.forEach(instance -> {
                list.add(new NacosRPCServiceInstance(instance));
            });
            serviceMap.put(serviceName,list);
        }
    }

    public class WeightedRoundRobinPolicy implements LoadBalancePolicy {

        /**
         * @param serviceName
         * @return
         */
        @Override
        public RPCServiceInstance selectServiceInstance(String serviceName) {
            CopyOnWriteArrayList<NacosRPCServiceInstance> list = serviceMap.get(serviceName);
            double totalWeight = list.stream().mapToDouble(RPCServiceInstance::getWeight).sum();
            NacosRPCServiceInstance nextInstance=null;
            double maxWeight=Double.MIN_VALUE;
           for (NacosRPCServiceInstance instance:list)
           {
               instance.setCurrentWeight(instance.getCurrentWeight()+instance.getWeight());
               if (instance.getCurrentWeight()>maxWeight)
               {
                   maxWeight=instance.getCurrentWeight();
                   nextInstance=instance;
               }
           }
            Assert.isTrue(nextInstance!=null,"no match service");
           nextInstance.setCurrentWeight(nextInstance.getCurrentWeight()-totalWeight);
           return nextInstance;
        }
    }

    public class WeightedRandomPolicy implements LoadBalancePolicy {
        /**
         * @param serviceName
         * @return
         */
        @Override
        public RPCServiceInstance selectServiceInstance(String serviceName) {
            CopyOnWriteArrayList<NacosRPCServiceInstance> list = serviceMap.get(serviceName);
            double totalWeight= list.stream().mapToDouble(RPCServiceInstance::getWeight).sum();
            double randomValue  = new Random().nextDouble(totalWeight) * totalWeight;
            double currentWeight=0;

            for (RPCServiceInstance instance:list)
            {
                currentWeight+=instance.getWeight();
                if (currentWeight>randomValue)
                {
                    return instance;
                }
            }
            return list.get(list.size()-1);
        }
    }
}
