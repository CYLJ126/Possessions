package com.zhangsc.netty.nettyinaction.cha13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @ClassName LogEventEncoder  ✺
 * @Description ✻ 代码清单13-2 LogEventEncoder
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/9 17:09 ✾
 * @Version 1.0.0 ✵
 **/
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
    private final InetSocketAddress remoteAddress;

    public LogEventEncoder(InetSocketAddress remoteAddress) {
        //LogEventEncoder创建了即将被发送到指定的InetSocketAddress的DatagramPacket消息
        this.remoteAddress = remoteAddress;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
        byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8);
        byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = ctx.alloc().buffer(file.length + msg.length + 1);
        //将文件名写入
        buf.writeBytes(file);
        //添加一个分隔符
        buf.writeByte(LogEvent.SEPARATOR);
        //将日志消息写入
        buf.writeBytes(msg);
        //将一个拥有数据和目的地地址的新DatagramPacket添加到出站的消息列表中
        out.add(new DatagramPacket(buf, remoteAddress));
    }
}
