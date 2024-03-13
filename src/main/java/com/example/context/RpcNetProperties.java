package com.example.context;

import com.example.protocol.Enums.ChannelType;
import com.example.protocol.Enums.SerializableType;
import lombok.Data;

@Data
public class RpcNetProperties {


    ChannelType channelType;

    long requestTimeOut;

    String localAddress;


    ProtocolProperties properties;


}
