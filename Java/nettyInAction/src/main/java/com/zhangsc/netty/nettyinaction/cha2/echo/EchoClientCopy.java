package com.zhangsc.netty.nettyinaction.cha2.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

/**
 * @ClassName EchoClientCopy  ✺
 * @Description ✻ 代码清单2-4 客户端的主类 的复制
 * 测试hanlder的顺序
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/7 23:21 ✾
 * @Version 1.0.0 ✵
 **/
public class EchoClientCopy {
    private final String host;
    private final int port;

    public EchoClientCopy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new EmbededChannelHandlerSimple2());
                            pipeline.addLast(new EmbededChannelHandlerSimple1());
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        /*if (args.length != 2) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() + "<host> <port>");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();*/
        new EchoClientCopy("127.0.0.1", 9898).start();
    }

    private class EmbededChannelHandlerSimple1 extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println("EmbededChannelHandlerSimple 1...");
        }

        @Override
        public void channelActive(ChannelHandlerContext context) {
            //当被通知Channel是活跃的时候，发送一条消息
            context.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        }
    }

    private class EmbededChannelHandlerSimple2 extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println("EmbededChannelHandlerSimple 2...");
            /**
             * zhangsc, ???
             * 在引导配置过程中添加了两个handler，顺序为EmbededChannelHandlerSimple2，EmbededChannelHandlerSimple1
             * 在两个handler的channelRead0()方法中分别打印了一行日志
             * 预期效果：
             * 1.打印EmbededChannelHandlerSimple 2...
             * 2.接着打印EmbededChannelHandlerSimple 1...
             *
             * 如果不加ctx.fireChannelRead(msg.retain());这一行是不会打印的，我一定要调用retain()方法才能正常运行，
             * 但是书上解释SimpleChannelInboundHandler类的channelRead0()方法会自动释放资源，我在
             * EmbededChannelHandlerSimple1中也没有用到消息，为什么在这儿调用retain()增加引用计数才不会出错？
             */
            //不加这句的话EmbededChannelHandlerSimple 1...不会打印
            ctx.fireChannelRead(msg.retain());
        }
    }

    private class EmbededChannelHandlerAdapter1 extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("anonymous handler 1...");
            ctx.fireChannelRead(msg);
        }

        @Override
        public void channelActive(ChannelHandlerContext context) {
            //当被通知Channel是活跃的时候，发送一条消息
            context.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        }
    }

    private class EmbededChannelHandlerAdapter2 extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("anonymous handler 2...");
            ctx.fireChannelRead(msg);
        }
    }
}

