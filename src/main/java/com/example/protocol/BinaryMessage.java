package com.example.protocol;

import com.example.protocol.Enums.MessageType;
import com.example.protocol.Enums.Status;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

@Getter
public class BinaryMessage extends AbstractMessage {

    Map<Integer,Integer> headerMap;
    byte[] content;
    int headerSize;
    int contentSize;


    public BinaryMessage(MessageType messageType, boolean requiredResponse, Map<Integer, Integer> headerMap, int seq, Status status, byte[] content) {
        super(headerMap,messageType,requiredResponse,headerMap.size()*2+1+ content.length,seq,status);
        this.headerSize = headerMap.size() * 2 + 1;
        this.contentSize = size - headerSize;
    }

    /**
     * @return 返回二进制流的报文内容
     */
    @Override
    public byte[] getContent() {
        return content;
    }
}
