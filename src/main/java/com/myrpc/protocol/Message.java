package com.myrpc.protocol;

import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.protocol.Enums.Status;

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
