package com.myrpc.protocol.content;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.myrpc.net.ResponseContentJsonDeserializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应信息的包装类，注意必须要在使用前检查请求是否成功
 */
@Data
@JsonDeserialize(using = ResponseContentJsonDeserializer.class)
public class ResponseContent implements Content, Serializable {

    /**
     * 远程调用的返回值，如果无需返回值则为空，如果远程调用出错则返回的result类型为String类型表示调用的错误信息
     */
    Object result;


    /**
     * 是否调用异常
     */
    boolean isSuccessful;

    /**
     * 响应结果的类名
     */
    String resultClass;

}
