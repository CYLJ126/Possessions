package com.zhangsc.netty.nettyinaction.cha9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName FrameChunkDecoderTest  ✺
 * @Description ✻ 代码清单9-6 测试FrameChunkDecoder
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/7 0:19 ✾
 * @Version 1.0.0 ✵
 **/
public class FrameChunkDecoderTest {
    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));
        //向它写入2字节，并断言它们将会产生一个新帧
        Assert.assertTrue(channel.writeInbound(input.readBytes(2)));
        try {
            //写入一个4字节大小的帧，并捕获预期的TooLongFrameException
            channel.writeInbound(input.readBytes(4));
            //如果上面没有抛出异常，那么就会到达这个断言，并且测试失败
            Assert.fail();
        } catch (TooLongFrameException e) {
            //expected exception
        }
        //写入剩余的3字节，并断言将会产生一个有效帧
        Assert.assertTrue(channel.writeInbound(input.readBytes(3)));
        Assert.assertTrue(channel.finish());

        //读取产生的消息，并且验证值
        //read frames
        ByteBuf read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(2), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.skipBytes(4).readSlice(3), read);
        read.release();
        buf.release();
    }
}
