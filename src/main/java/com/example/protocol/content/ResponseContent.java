package com.example.protocol.content;

import lombok.Data;

@Data
public class ResponseContent {

    /**
     * 远程调用的返回值，如果无需返回值则为空，如果远程调用出错则返回的result类型为String类型表示调用的错误信息
     */
    Object result;

    /**
     * 调用返回的值的类型，如果调用错误则类型为异常
     */
    String resultClassName;

}
