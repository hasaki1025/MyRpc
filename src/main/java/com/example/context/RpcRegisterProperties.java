package com.example.context;

import com.example.protocol.Enums.RegisterType;
import lombok.Data;

import java.util.Properties;
@Data
public class RpcRegisterProperties {

    Properties properties;
    String address;
    String ip;
    int port;

    RegisterType registerType;




}
