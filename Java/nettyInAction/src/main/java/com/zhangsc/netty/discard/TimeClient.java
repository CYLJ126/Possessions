package com.zhangsc.netty.discard;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpChannel;
import io.netty.channel.socket.SocketChannel;

/**
 * @ClassName TimeClient
 * @Description: 时间客户端
 * @Author Zhangsc
 * @Date 2020/1/21
 * @Version V1.0
 **/
public class TimeClient {
    public static void main(String[] args) throws InterruptedException {
        String host = "10.60.45.149";
        int port = 7777;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            /**
             * 1.BootStrap 和 ServerBootstrap 类似,不过他是对非服务端的 channel 而言，
             * 比如客户端或者无连接传输模式的 channel。
             */
            Bootstrap bootstrap = new Bootstrap();
            /**
             * 2.如果你只指定了一个 EventLoopGroup，那他就会既作为一个 boss group ，
             * 也会作为一个 workder group，尽管客户端不需要使用到 boss worker 。
             */
            bootstrap.group(workerGroup);
            /**
             * 3.代替NioServerSocketChannel的是NioSocketChannel,这个类在客户端channel
             * 被创建时使用。
             */
            bootstrap.channel(NioSctpChannel.class);
            /**
             * 4.不像在使用 ServerBootstrap 时需要用 childOption() 方法，因为客户端的
             * SocketChannel 没有父亲。
             */
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });
            /**
             * 5.我们用 connect() 方法代替了 bind() 方法。
             */
            //启动客户端
            ChannelFuture f = bootstrap.connect(host, port).sync();
            //等待连接关闭
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
