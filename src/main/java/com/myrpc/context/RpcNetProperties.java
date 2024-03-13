package com.myrpc.context;

import com.myrpc.protocol.Enums.ChannelType;
import lombok.Data;

@Data
public class RpcNetProperties {


    ChannelType channelType;

    long requestTimeOut;

    String ip;
    int port;


    SSLProperties sslProperties;


    ProtocolProperties protocolProperties;


}
