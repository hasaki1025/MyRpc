package com.example;

import com.example.exception.MessageReadException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyRpcApplicationTests {

    @Test
    void contextLoads() throws MessageReadException {


       /* HashMap<Integer, Integer> headersMap = new HashMap<>();
        headersMap.put(1,1);
        headersMap.put(2,1);
        headersMap.put(3,1);

        String s = "1";
        BinaryMessage message = new BinaryMessage(MessageType.request,true,headersMap,1,Status.OK,s.getBytes());

        ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
        ByteBuf buffer = allocator.buffer();
       MessageUtil.messageToBytes(message,buffer);
        BinaryMessage m2 = MessageUtil.bytesToDefaultMessage(buffer);
        System.out.println(m2);*/
    }

}
