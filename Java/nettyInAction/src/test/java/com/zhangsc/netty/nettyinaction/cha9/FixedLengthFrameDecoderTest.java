package com.zhangsc.netty.nettyinaction.cha9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName FixedLengthFrameDecoderTest  ✺
 * @Description ✻ 代码清单9-2 测试FixedLengthFrameDecoder
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/6 23:22 ✾
 * @Version 1.0.0 ✵
 **/
public class FixedLengthFrameDecoderTest {
    @Test
    public void testFrameDecoded() {
        //创建一个ByteBuf，并存储9字节
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        //创建一个EmbeddedChannel，并添加一个FixedLengthFrameDecoder，其将以3字节的帧长度被测试
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        //write bytes
        Assert.assertTrue(channel.writeInbound(input.retain()));
        //标记channel为已完成状态
        Assert.assertTrue(channel.finish());

        //读取所生成的消息，并且验证是否有3帧（切片），其中每帧（切片）为3字节
        //read messages
        ByteBuf read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();

        Assert.assertNull(channel.readInbound());
        buf.release();
    }

    @Test
    public void testFramsesDecoded2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        //返回false，因为没有一个完整的可供读取的帧
        Assert.assertFalse(channel.writeInbound(input.readBytes(2)));
        Assert.assertTrue(channel.writeInbound(input.readBytes(7)));

        Assert.assertTrue(channel.finish());
        ByteBuf read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);

        Assert.assertNull(channel.readInbound());
        buf.release();
    }
}
