package com.example.protocol;

import com.example.protocol.Enums.MessageType;
import com.example.protocol.Enums.Status;
import com.example.protocol.content.ResponseContent;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class RPCResponse extends AbstractMessage {


    ResponseContent content;

    public RPCResponse(Map<Integer, Integer> headerMap, MessageType messageType, boolean requiredResponse, int size, int seq, Status status) {
        super(headerMap, messageType, requiredResponse, size, seq, status);
    }

}
