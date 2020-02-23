package com.zhangsc.netty.nettyguide.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @ClassName DiscardServer
 * @Description: 启动服务
 * @Author Zhangsc
 * @Date 2020/1/21
 * @Version V1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
public class DiscardServer {
    private int port;

    public void run() throws InterruptedException {
        /**
         * 1.NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，Netty 提供了许多不同的
         * EventLoopGroup 的实现用来处理不同的传输。在这个例子中我们实现了一个服务端的应用，
         * 因此会有2个 NioEventLoopGroup 会被使用。第一个经常被叫做‘boss’，用来接收进来的
         * 连接。第二个经常被叫做‘worker’，用来处理已经被接收的连接，一旦‘boss’接收到连接，
         * 就会把连接信息注册到‘worker’上。如何知道多少个线程已经被使用，如何映射到已经创
         * 建的 Channel上都需要依赖于 EventLoopGroup 的实现，并且可以通过构造函数来配置
         * 他们的关系。
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            /**
             * 2.ServerBootstrap 是一个启动 NIO 服务的辅助启动类。你可以在这个服务中直接
             * 使用 Channel，但是这会是一个复杂的处理过程，在很多情况下你并不需要这样做。
             */
            ServerBootstrap bootstrap = new ServerBootstrap();
            /**
             * 3.这里我们指定使用 NioServerSocketChannel 类来举例说明一个新的 Channel
             * 如何接收进来的连接。
             * .channel()方法
             *
             * 4.这里的事件处理类经常会被用来处理一个最近的已经接收的 Channel。
             * ChannelInitializer 是一个特殊的处理类，他的目的是帮助使用者配置一个新
             * 的 Channel。也许你想通过增加一些处理类比如DiscardServerHandler 来配
             * 置一个新的 Channel 或者其对应的ChannelPipeline 来实现你的网络程序。
             * 当你的程序变得复杂时，可能你会增加更多的处理类到 pipline 上，然后提
             * 取这些匿名类到最顶层的类上。
             * .childHandler()方法
             *
             * 5.你可以设置这里指定的 Channel 实现的配置参数。我们正在写一个TCP/IP
             * 的服务端，因此我们被允许设置 socket 的参数选项比如tcpNoDelay 和
             * keepAlive。请参考 ChannelOption 和详细的 ChannelConfig 实现的接口
             * 文档以此可以对ChannelOption 的有一个大概的认识。
             * .option()方法
             *
             * 6.你关注过 option() 和 childOption() 吗？option() 是提供给
             * NioServerSocketChannel 用来接收进来的连接。childOption() 是提供
             * 给由父管道ServerChannel 接收到的连接，在这个例子中也是
             * NioServerSocketChannel。
             * .childOption()方法
             */
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            /**
             * 7.我们继续，剩下的就是绑定端口然后启动服务（main方法中）。这里我们在
             * 机器上绑定了机器所有网卡上的 7777 端口。当然现在你可以多次调用 bind()
             * 方法(基于不同绑定地址)。
             */
            // 绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(port).sync();

            // 等待服务器  socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 7777;
        }
        new DiscardServer(port).run();
    }
}