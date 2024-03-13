package com.example.protocol;

import com.example.protocol.Enums.SerializableType;

public interface Message {

    int getVersion();
    SerializableType getSerializableType();
    boolean isResponse();
    boolean hasContent();


    int size();
    Object content();
    int getSeq();

    void setSeq(int seq);
}
