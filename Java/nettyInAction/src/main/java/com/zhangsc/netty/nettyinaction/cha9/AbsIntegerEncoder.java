package com.zhangsc.netty.nettyinaction.cha9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @ClassName AbsIntegerEncoder  ✺
 * @Description ✻ 代码清单9-3 AbsIntegerEncoder
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/6 23:48 ✾
 * @Version 1.0.0 ✵
 **/
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() >= 4) {
            //计算下一个整数的绝对值
            int value = Math.abs(in.readInt());
            //将该整数写入到编码消息的List中
            out.add(value);
        }
    }
}
