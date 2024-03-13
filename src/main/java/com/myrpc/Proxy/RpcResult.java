package com.myrpc.Proxy;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResult<T>  implements Serializable {

    T result;

}
