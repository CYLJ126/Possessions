package com.zhangsc.netty.nettyinaction.cha10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @ClassName ToIntegerDecoder2  ✺
 * @Description ✻ 代码清单10-2 ToIntegerDecoder2类扩展了ReplayingDecoder
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 14:41 ✾
 * @Version 1.0.0 ✵
 **/
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readInt());
    }
}
