package com.myrpc.Util;

import com.myrpc.exception.MessageReadException;
import com.myrpc.protocol.BinaryMessage;
import com.myrpc.protocol.Enums.MessageType;
import com.myrpc.protocol.Enums.SerializableType;
import com.myrpc.protocol.Enums.Status;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 *
 */
@Slf4j
public class MessageUtil {


    public static final byte[] MAGIC_NUMBER ={'<','>'};
    public static final String MAGIC =new String(MAGIC_NUMBER);

    public static final byte HEADER_END= (byte) 0b11111111;

    public static SerializableType DEFAULT_SERIALIZABLETYPE=SerializableType.JSON;

    public static int CONST_MESSAGE_SIZE=10;
    public static int CONST_MESSAGE_HEAD_LENGTH=7;

    public static int LENGTH_FIELD_SIZE=2;


    public static int countSize(BinaryMessage binaryMessage)
    {
        return countSize(binaryMessage.getHeaders(), binaryMessage.getContent());
    }public static int countSize(Map<Integer,Integer> headers, byte[] content)
    {
        return headers.size()*2+1+content.length;
    }

    public static int countHeaderSize(Map<Integer, Integer> headers)
    {
        return headers.size()*2+1;
    }




    public static BinaryMessage bytesToBinaryMessage(ByteBuf buf) throws MessageReadException {
        //BinaryMessage message = new BinaryMessage();


        //固定首部读取
        byte[] magicNum = new byte[MAGIC_NUMBER.length];
        buf.readBytes(magicNum);
        if (!MAGIC.equals(new String(magicNum)))
            throw new MessageReadException("MagicNumber Error");
        byte b = buf.readByte();
        MessageType msgType = MessageType.forInt((b & 0x80)==0 ? 0 : 1);
        boolean requiredResponse=false;
        if (msgType.equals(MessageType.request))
            requiredResponse  = (b & 0x40) != 0;
        //TODO bug
        Status status = Status.forInt(b & 0x3F);


        int seq = buf.readInt();

        short size = buf.readShort();

        //读取扩展首部
        byte id=buf.readByte();
        HashMap<Integer, Integer> headers = new HashMap<>();
        while (id!=HEADER_END)
        {
            headers.put((int) id, (int) buf.readByte());
            id= buf.readByte();
        }


        //读取数据载荷部分
        byte[] content = new byte[size-2*headers.size()-1];
        buf.readBytes(content);

        return new BinaryMessage(headers,content,msgType,requiredResponse,size,seq,status);
    }

    public static void messageToBytes(BinaryMessage message, ByteBuf buf)
    {

        buf.writeBytes(MAGIC_NUMBER);

        byte b= (byte) message.getStatus().getValue();
        if (message.getMessageType().equals(MessageType.response))
            b = (byte) (b | 0b10000000);
        if (message.isRequiredResponse())
            b = (byte) (b | 0b01000000);
        buf.writeByte(b);


        buf.writeInt(message.getSeq());


        buf.writeShort(message.getSize());

        message.getHeaders().forEach((k,v)->{
            buf.writeByte(k.byteValue());
            buf.writeByte(v.byteValue());
        });
        buf.writeByte(HEADER_END);
        buf.writeBytes(message.getContent());
    }










}
