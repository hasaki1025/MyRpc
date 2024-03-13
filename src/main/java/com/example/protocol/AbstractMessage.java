package com.example.protocol;

import com.example.protocol.Enums.MessageType;
import com.example.protocol.Enums.Status;
import lombok.Data;

import java.util.Map;

@Data
public abstract class AbstractMessage implements Message {

    final Map<Integer,Integer> headerMap;
    final MessageType messageType;
    final boolean requiredResponse;
    final int size;
    final int seq;

    final Status status;

    public AbstractMessage(Map<Integer, Integer> headerMap, MessageType messageType, boolean requiredResponse, int size, int seq, Status status) {
        this.headerMap = headerMap;
        this.messageType = messageType;
        this.requiredResponse = requiredResponse;
        this.size = size;
        this.seq = seq;
        this.status = status;
    }
}
