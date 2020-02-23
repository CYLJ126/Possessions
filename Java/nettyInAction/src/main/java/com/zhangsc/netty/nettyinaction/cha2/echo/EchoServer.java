package com.zhangsc.netty.nettyinaction.cha2.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @ClassName EchoServer  ✺
 * @Description ✻ EchoServer服务端启动 代码清单2-2 EchoServer 类
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/1/30 10:00 ✾
 * @Version 1.0.0 ✵
 **/
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        /*if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
        }
        //设置端口值（如果端口参数的格式不正确，则抛出一个NumberFormatException）
        int port = Integer.parseInt(args[0]);*/
        int port = 9898;
        //调用服务器的start()方法
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    //指定所使用的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址，服务器将绑定到这个地址以监听新的连接请求。
                    .localAddress(new InetSocketAddress(port))
                    /**
                     * 添加一个EchoServerHandler到子Channel的ChannelPipeline
                     *
                     * 当一个新的连接被接受时，一个新的子Channel将会被创建，而ChannelInitializer将会把一个你的EchoServerHandler的实例添加到该Channel的ChannelPipeline中。正如我们之前所解释的，这个ChannelHandler将会收到有关入站消息的通知。
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            //EchoServerHandler被标注为@Shareable，所以我们可以总是使用同样的实例
                            //这里对于所有的客户端连接来说，都会使用同一个EchoServerHandler，因为其被标注为@Sharable
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });
            //异步地绑定服务器；调用sync()方法阻塞等待直到绑定完成
            //对sync()方法的调用将导致当前Thread阻塞，一直到绑定操作完成为止
            ChannelFuture f = b.bind().sync();
            //获取Channel的CloseFuture，并且阻塞当前线程直到它完成
            //该应用程序将会阻塞等待直到服务器的Channel关闭（因为你在Channel的CloseFuture上调用了sync()方法）
            f.channel().closeFuture().sync();
        } finally {
            //关闭EventLoopGroup，释放所有的资源，包括所有被创建的线程
            group.shutdownGracefully().sync();
        }
    }
}
