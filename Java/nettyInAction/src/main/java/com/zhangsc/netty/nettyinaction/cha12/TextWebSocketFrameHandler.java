package com.zhangsc.netty.nettyinaction.cha12;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @ClassName TextWebSocketFrameHandler  ✺
 * @Description ✻ 代码清单12-2 处理文本帧
 * 代码清单12-2 展示了我们用于处理TextWebSocketFrame的ChannelInboundHandler，
 * 其还将在它的ChannelGroup中跟踪所有活动的WebSocket连接。
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/9 15:05 ✾
 * @Version 1.0.0 ✵
 **/
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //增加消息的引用计数，它写到ChannelGroup中所有已经连接的客户端
        group.writeAndFlush(msg.retain());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            //如果该事件表示握手成功，则从该ChannelPipeline中移除HttpRequestHandler，四面边声连角起将不会接收到任何HTTP消息了
            context.pipeline().remove(HttpRequestHandler.class);
            //通知所有已经连接的WebSocket客户端新的客户端已经连接上了
            group.writeAndFlush(new TextWebSocketFrame("Client " + context.channel() + " joined"));
            //将新的WebSocket Channel添加到ChannelGroup中，以便它可以接收到所有的消息
            group.add(context.channel());
        } else {
            super.userEventTriggered(context, evt);
        }
    }
}
