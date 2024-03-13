package com.example.net;

public interface EncipherConvertor {


    /**
     * @param msg 原始数据
     * @return 加密后的数据
     */
    byte[] encode(byte[] msg);

    /**
     * @param msg 未解密的数据流
     * @return 解密后的数据流
     */
    byte[] decode(byte[] msg);
}
