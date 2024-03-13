package com.myrpc.protocol.content;

import lombok.Data;

@Data
public class RequestContent implements Content{

    /**
     * 调用类名
     */
    String serviceName;


    /**
     * 调用方法名称
     */
    String methodName;


    /**
     * 调用参数
     */
    Object[] args;








}
