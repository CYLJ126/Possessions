package com.zhangsc.netty.nettyinaction.cha2.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Objects;

/**
 * @ClassName EchoServerHandler  ✺
 * @Description ✻ 该Echo服务器的ChannelHandler实现是EchoServerHandler 代码清单2-1 EchoServerHandler
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/1/30 9:35 ✾
 * @Version 1.0.0 ✵
 **/
//标示一个Channelandler可以被多个Channel安全地共享
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        //将消息记录到控制台
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        //将接收到的消息写给发送者，而不冲刷出站消息
        context.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        //将未决消息冲刷到远程节点，并且关闭该Channel
        context.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable casuse) {
        casuse.printStackTrace();
        //关闭该Channel
        context.close();
    }
}
