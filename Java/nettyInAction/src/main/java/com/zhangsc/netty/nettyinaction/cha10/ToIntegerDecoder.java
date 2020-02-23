package com.zhangsc.netty.nettyinaction.cha10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @ClassName ToIntegerDecoder  ✺
 * @Description ✻ 代码清单10-1 ToIntegerDecoder 类扩展了ByteToMessageDecoder
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 14:31 ✾
 * @Version 1.0.0 ✵
 **/
public class ToIntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 4) {
            out.add(in.readInt());
        }
    }
}
