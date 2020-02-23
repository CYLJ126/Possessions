package com.zhangsc.netty.nettyinaction.cha13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @ClassName LogEventDecoder  ✺
 * @Description ✻ 代码清单13-6 LogEventDecoder
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/9 22:12 ✾
 * @Version 1.0.0 ✵
 **/
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out)
            throws Exception {
        //获取对DatagramPacket中的数据（ByteBuf）的引用
        ByteBuf data = datagramPacket.content();
        //获取该SEPARATOR的索引
        int idx = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR);
        //文件名
        String filename = data.slice(0, idx).toString(CharsetUtil.UTF_8);
        //日志消息
        String logMsg = data.slice(idx + 1, data.readableBytes()).toString(CharsetUtil.UTF_8);
        //构建一个新的LogEvent对象，并且将它添加到（已经解码的消息的）列表中
        LogEvent event =
                new LogEvent(datagramPacket.sender(), System.currentTimeMillis(), filename, logMsg);
        out.add(event);
    }
}
