package com.example.context;

import com.example.protocol.Enums.EncryptionMethod;
import com.example.protocol.Enums.SerializableType;
import lombok.Data;

@Data
public class ProtocolProperties{

     SerializableType serializableType;

     EncryptionMethod encryptionMethod;


}