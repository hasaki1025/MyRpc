package com.myrpc.Factory;

import com.myrpc.context.RpcProperties;
import com.myrpc.net.*;
import com.myrpc.protocol.Enums.SerializableType;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SerializableConvertorFactory  {


    SerializableType serializableType;

    Class<? extends SerializableConvertor > serializableTypeClass;


    public SerializableConvertorFactory(RpcProperties rpcProperties) throws Exception {
        this.serializableType = rpcProperties.getRpcNetProperties().getProtocolProperties().getSerializableType();
        serializableTypeClass=SerializableType.toSerializableTypeClass(this.serializableType);
    }

    /**
     * @return
     * @throws Exception
     */
    @Bean
    public SerializableConvertor serializableConvertor() throws Exception {
        if (serializableType.equals(SerializableType.JAVA))
            return new JAVAObjectConvertor();
        else if (serializableType.equals(SerializableType.JSON))
            return new JSONConvertor();
        else if (serializableType.equals(SerializableType.MESSAGE_PACK))
            return new MessagePackerConvertor();
        throw new Exception("no match SerializableConvertor");
    }


}
