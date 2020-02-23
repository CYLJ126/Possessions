package com.zhangsc.netty.nettyinaction.cha9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName AbsIntegerEncoderTest  ✺
 * @Description ✻ 代码清单9-4 测试AbsIntegerEncoder
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/6 23:52 ✾
 * @Version 1.0.0 ✵
 **/
public class AbsIntegerEncoderTest {
    @Test
    public void testEncoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            //写入9个负整数
            buf.writeInt(i * -1);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        //写入ByteBuf，并断言调用readOutbound()方法将会产生数据
        Assert.assertTrue(channel.writeOutbound(buf));
        //将channel标记为已完成状态
        Assert.assertTrue(channel.finish());

        //read bytes
        for (int i = 1; i < 10; i++) {
            //读取所产生的消息，并断言它们包含了对应的绝对值
            Assert.assertEquals(i, (int) channel.readOutbound());
        }

        Assert.assertNull(channel.readOutbound());
    }
}
