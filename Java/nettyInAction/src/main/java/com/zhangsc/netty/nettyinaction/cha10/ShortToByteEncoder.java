package com.zhangsc.netty.nettyinaction.cha10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName ShortToByteEncoder  ✺
 * @Description ✻ 代码清单10-5 ShortToByteEncoder类
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 15:13 ✾
 * @Version 1.0.0 ✵
 **/
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
        out.writeShort(msg);
    }
}
