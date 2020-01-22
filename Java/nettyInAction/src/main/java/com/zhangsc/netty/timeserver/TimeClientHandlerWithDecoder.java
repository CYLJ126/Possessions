package com.zhangsc.netty.timeserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TimeClientHandlerWithDecoder
 * @Description: 处理客户   端 channel
 * @Author Zhangsc
 * @Date 2020/1/21
 * @Version V1.0
 **/
@Slf4j
public class TimeClientHandlerWithDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(final ChannelHandlerContext context) {
        final ByteBuf time = context.alloc().buffer(4);
        //2208988800L Mon Jan 26 21:36:28 CST 1970
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        final ChannelFuture future = context.writeAndFlush(time);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                assert future == f;
                context.close();
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        context.write(message);
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        context.close();
    }
}
