package com.zhangsc.netty.nettyinaction.cha4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.handler.ssl.OpenSsl;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @ClassName NettyOioServer  ✺
 * @Description ✻ 代码清单4-3 使用Netty的阻塞网络处理
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/1/30 19:11 ✾
 * @Version 1.0.0 ✵
 **/
public class NettyOioServer {
    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    //使用OioEventLoopGroup以允许阻塞模式（旧的I/O）
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //添加一个ChannelInboundHandlerAdapter以拦截和处理事件
                            socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext context) {
                                    //将消息写到客户端，并添加ChannelFutureListener，以便消息一被写完就关闭连接
                                    //效果为只要客户端连接上就会接收到数据且立马关闭连接
                                    context.writeAndFlush(buf.duplicate())
                                            .addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });
            //绑定服务器以接受连接
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        NettyOioServer server = new NettyOioServer();
        server.server(9898);
    }
}
