package com.zhangsc.netty.nettyinaction.cha10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * @ClassName SafeByteToMessageDecoder  ✺
 * @Description ✻ 代码清单10-4 TooLongFrameException
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 15:00 ✾
 * @Version 1.0.0 ✵
 **/
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {
    private static final int MAX_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readable = in.readableBytes();
        if (readable > MAX_FRAME_SIZE) {
            //跳过所有的可读字节并抛出TooLongFrameException通知ChannelHandler
            in.skipBytes(readable);
            throw new TooLongFrameException("Frame too big!");
        }
        //do something
    }
}
