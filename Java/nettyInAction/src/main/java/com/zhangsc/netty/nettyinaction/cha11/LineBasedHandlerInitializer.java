package com.zhangsc.netty.nettyinaction.cha11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * @ClassName LineBasedHandlerInitializer  ✺
 * @Description ✻ 代码清单11-8 处理由行尾符分隔的帧
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 19:39 ✾
 * @Version 1.0.0 ✵
 **/
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //该LineBasedFrameDecoder将提取的帧转发给下一个ChannelInboundHandler
        pipeline.addLast(new LineBasedFrameDecoder(64 * 1024));
        //添加FrameHandler以接收帧
        pipeline.addLast(new FrameHandler());
    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            //该方法传入了单个帧的内容
            //Do something with the data extracted from the frame
        }
    }
}
