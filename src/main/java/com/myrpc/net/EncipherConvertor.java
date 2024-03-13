package com.myrpc.net;

public interface EncipherConvertor {


    /**
     * @param msg 原始数据
     * @return 加密后的数据
     */
    byte[] encrypt(byte[] msg) throws Exception;

    /**
     * @param msg 未解密的数据流
     * @return 解密后的数据流
     */
    byte[] decrypt(byte[] msg) throws Exception;
}
