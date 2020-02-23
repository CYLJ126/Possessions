package com.zhangsc.netty.nettyguide.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * handler 是由 Netty 生成用来处理 I/O 事件的。
 * <p>
 * 1.SimpleChatServerHandler 继承自 SimpleChannelInboundHandler，这个类实现了ChannelInboundHandler接口，
 * ChannelInboundHandler 提供了许多事件处理的接口方法，然后你可以覆盖这些方法。现在仅仅只需要继承
 * SimpleChannelInboundHandler 类而不是你自己去实现接口方法。
 */
@Slf4j
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 2.覆盖了 handlerAdded() 事件处理方法。每当从服务端收到新的客户端连接时，客户端的 Channel 存入
     * ChannelGroup列表中，并通知列表中的其他客户端 Channel
     *
     * @param context
     */
    @Override
    public void handlerAdded(ChannelHandlerContext context) {
        Channel incoming = context.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + "加入\n");
        }
        // 加入ChannelGroup列表中
        channels.add(context.channel());
    }

    /**
     * 3.覆盖了 handlerRemoved() 事件处理方法。每当从服务端收到客户端断开时，客户端的 Channel 移除
     * ChannelGroup 列表中，并通知列表中的其他客户端 Channel
     *
     * @param context
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext context) {
        Channel incoming = context.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
        }
        // 从ChannelGroup列表中移除
        channels.remove(context.channel());
    }

    /**
     * 4.覆盖了 channelRead0() 事件处理方法。每当从服务端读到客户端写入信息时，将信息转发给其他
     * 客户端的 Channel。其中如果你使用的是 Netty 5.x 版本时，需要把 channelRead0() 重命名为
     * messageReceived()
     *
     * @param context
     * @param s
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, String s) {
        Channel incoming = context.channel();
        // 收到信息时，将信息转发给其他客户端
        for (Channel channel : channels) {
            if (channel != incoming) {
                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n");
            } else {
                channel.writeAndFlush("[you]" + s + "\n");
            }
        }
    }

    /**
     * 5.覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动。
     *
     * @param context
     */
    @Override
    public void channelActive(ChannelHandlerContext context) {
        Channel incoming = context.channel();
        log.info("SimpelChatClient: " + incoming.remoteAddress() + "在线");
    }

    /**
     * 6.覆盖了 channelInactive() 事件处理方法。服务端监听到客户端不活动。
     *
     * @param context
     */
    @Override
    public void channelInactive(ChannelHandlerContext context) {
        Channel incoming = context.channel();
        log.info("SimpelChatClient: " + incoming.remoteAddress() + "掉线");
    }

    /**
     * 7.exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，即当 Netty 由于
     * IO 错误或者处理器在处理事件时抛出的异常时。在大部分情况下，捕获的异常应该被记录下
     * 来并且把关联的 channel 给关闭掉。然而这个方法的处理方式会在遇到不同异常的情况下有
     * 不同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
     *
     * @param context
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        Channel incoming = context.channel();
        log.info("SimpelChatClient: " + incoming.remoteAddress() + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        incoming.close();
    }
}
