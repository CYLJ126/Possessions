package com.zhangsc.netty.nettyinaction.cha11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @ClassName LengthBasedInitializer  ✺
 * @Description ✻ 代码清单11-10 使用LengthFieldBasedFrameDecoder解码器基于长度的协议
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 20:13 ✾
 * @Version 1.0.0 ✵
 **/
public class LengthBasedInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //使用LengthFieldBasedFrameDecoder解码器将帧长度编码到帧起始的前8个字节中的消息
        pipeline.addLast(new LengthFieldBasedFrameDecoder(64 * 1024, 0, 8));
        //添加FramdeHandler以处理每个帧
        pipeline.addLast(new FramdeHandler());
    }

    public static final class FramdeHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            //Do stomething with the frame
            //处理帧的数据
        }
    }
}
