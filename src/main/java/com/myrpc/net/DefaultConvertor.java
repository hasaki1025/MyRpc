package com.myrpc.net;

/**
 * 默认使用明文传输
 */
public class DefaultConvertor implements EncipherConvertor {

    /**
     * @param msg 原始数据
     * @return 原报文
     */
    @Override
    public byte[] encrypt(byte[] msg) {
        return msg;
    }

    /**
     * @param msg 未解密的数据流
     * @return 原报文
     */
    @Override
    public byte[] decrypt(byte[] msg) {
        return msg;
    }
}
