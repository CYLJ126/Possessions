package com.zhangsc.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @ClassName TimeClientHandler
 * @Description: TODO
 * @Author Zhangsc
 * @Date 2020/1/21
 * @Version V1.0
 **/
@Slf4j
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        /**
         * 1.在TCP/IP中，Netty 会把读到的数据放到 ByteBuf 的数据结构中。
         */
        ByteBuf byteBuf = (ByteBuf) message;
        try {
            long currentTimeMillis = (byteBuf.readUnsignedInt() - 2208988800L) * 1000L;
            log.info("time: {}", new Date(currentTimeMillis));
            context.close();
        } finally {
            byteBuf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        context.close();
    }
}
