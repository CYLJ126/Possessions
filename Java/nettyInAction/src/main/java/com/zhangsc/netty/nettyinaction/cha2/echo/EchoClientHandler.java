package com.zhangsc.netty.nettyinaction.cha2.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @ClassName EchoClientHandler  ✺
 * @Description ✻ 客户端逻辑处理
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/1/30 10:29 ✾
 * @Version 1.0.0 ✵
 **/
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    /**
     * 每当接收数据时，都会调用这个方法。需要注意的是，由服务器发送的消息可能会被分块接收。也就是说，如果服务器发送
     * 了5字节，那么不能保证这5字节会被一次性接收。即使是对于这么少量的数据，channelRead0()方法也可能会被调用两次，
     * 第一次使用一个持有3字节的ByteBuf（Netty的字节容器），第二次使用一个持有2字节的ByteBuf。作为一个面向流的协议，
     * TCP保证了字节数组将会按照服务器发送它们的顺序被接收。
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        //记录已接收消息的转储
        System.out.println("Client received: " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        //当被通知Channel是活跃的时候，发送一条消息
        context.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }
}
