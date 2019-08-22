package com.wjg.rpc.protocol;

import com.wjg.rpc.transport.InvokerProtocol;

import java.io.Serializable;

/**
 * 消息协议
 */
public class MyPotocolBean implements Serializable {

    //协议头
    private byte type;

    //版本号
    private byte flag;

    //内容长度
    private int length;

    //消息内容
    private InvokerProtocol invokerProtocol;

    //



}
