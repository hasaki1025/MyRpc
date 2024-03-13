package com.example.protocol.content;

import lombok.Data;

import java.lang.reflect.Method;
@Data
public class RequestContent {

    /**
     * 调用类名
     */
    String serviceName;

    /**
     * 服务版本号
     */
    String version;

    /**
     * 调用方法名称
     */
    String methodName;


    /**
     * 调用参数
     */
    Object[] args;








}
