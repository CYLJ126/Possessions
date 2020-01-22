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
    private ByteBuf byteBuf;

    /**
     * 1.ChannelHandler 有2个生命周期的监听方法：handlerAdded()和 handlerRemoved()。
     * 你可以完成任意初始化任务只要它不会被阻塞很长的时间。
     * @param context
     */
    @Override
    public void handlerAdded(ChannelHandlerContext context) {
        byteBuf = context.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext context) {
        byteBuf.release();
        byteBuf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {

        ByteBuf buf = (ByteBuf) message;
        /**
         * 2.首先，所有接收的数据都应该被累积在 buf 变量里。
         */
        byteBuf.writeBytes(buf);
        buf.release();

        /**
         * 3.然后，处理器必须检查 buf 变量是否有足够的数据，在这个例子中是4个字节，
         * 然后处理实际的业务逻辑。否则，Netty 会重复调用 channelRead() 当有更多
         * 数据到达直到4个字节的数据被积累。
         */
        if(byteBuf.readableBytes() >= 4) {
            long currentTimeMillis = (byteBuf.readUnsignedInt() - 2208988800L) * 1000L;
            log.info(String.valueOf(new Date(currentTimeMillis)));
            context.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        context.close();
    }
}
