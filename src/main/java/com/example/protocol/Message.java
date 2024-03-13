package com.example.protocol;

import com.example.protocol.Enums.MessageType;
import com.example.protocol.Enums.SerializableType;
import com.example.protocol.Enums.Status;

import java.util.Set;

public interface Message {


    MessageType getMessageType();
    boolean isRequiredResponse();


    /**
     * @return 此处返回size为扩展首部加数据载荷部分
     */
    int getSize();

    int getSeq();


    Status getStatus();


    Object getContent();

}
