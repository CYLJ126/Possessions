package com.zhangsc.netty.nettyinaction.cha9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * @ClassName FrameChunkDecoder  ✺
 * @Description ✻ 代码清单9-5 FrameChunkDecoder
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/7 0:16 ✾
 * @Version 1.0.0 ✵
 **/
public class FrameChunkDecoder extends ByteToMessageDecoder {
    private final int maxFrameSize;

    public FrameChunkDecoder(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        if (readableBytes > maxFrameSize) {
            //disccard the bytes
            in.clear();
            //如果该帧过大，则丢弃它并抛出一个TooLongFrameException
            throw new TooLongFrameException();
        }
        ByteBuf buf = in.readBytes(readableBytes);
        out.add(buf);
    }
}
