package com.zhangsc.netty.nettyinaction.cha13;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName LogEventHandler  ✺
 * @Description ✻ 代码清单13-7 LogEventHandler
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/9 22:27 ✾
 * @Version 1.0.0 ✵
 **/
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, LogEvent event) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(event.getReceivedTimestamp());
        builder.append(" [");
        builder.append(event.getSource().toString());
        builder.append("] [");
        builder.append(event.getLogfile());
        builder.append("] : ");
        builder.append(event.getMsg());
        System.out.println(builder.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
