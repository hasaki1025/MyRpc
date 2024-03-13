package com.myrpc.Factory;

import com.myrpc.net.JAVAObjectConvertor;
import com.myrpc.net.JSONConvertor;
import com.myrpc.net.ProtoBufferConvertor;
import com.myrpc.net.SerializableConvertor;
import com.myrpc.protocol.Enums.SerializableType;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SerializableConvertorFactory implements FactoryBean<SerializableConvertor> {


    SerializableType serializableType;

    Class<? extends SerializableConvertor > serializableTypeClass;


    public SerializableConvertorFactory(@Value("${MyRpc.net.protocol.SerializableType}")String serializable) {
        this.serializableType = SerializableType.valueOf(serializable);
        serializableTypeClass=SerializableType.toSerializableTypeClass(this.serializableType);
    }

    /**
     * @return
     * @throws Exception
     */
    @Override
    public SerializableConvertor getObject() throws Exception {
        if (serializableType.equals(SerializableType.JAVA))
            return new JAVAObjectConvertor();
        else if (serializableType.equals(SerializableType.JSON))
            return new JSONConvertor();
        else if (serializableType.equals(SerializableType.PROTO_BUFFER))
            return new ProtoBufferConvertor();
        throw new Exception("no match SerializableConvertor");
    }

    /**
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return serializableTypeClass;
    }

    /**
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
