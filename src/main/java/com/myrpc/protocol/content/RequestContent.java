package com.myrpc.protocol.content;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.myrpc.net.RequestContentJsonDeserializer;
import lombok.Data;

@Data
@JsonDeserialize(using = RequestContentJsonDeserializer.class)
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
     * 调用参数,序列化时需要将其转化为具体类
     */
    Object[] args;

    /**
     * 参数类型
     */
    String[] argClass;

}
