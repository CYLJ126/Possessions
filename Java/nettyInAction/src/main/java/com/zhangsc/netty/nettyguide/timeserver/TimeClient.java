package com.zhangsc.netty.nettyguide.timeserver;

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
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSctpChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandlerWithDecoder());
                }
            });
            //启动客户端
            ChannelFuture f = bootstrap.connect(host, port).sync();
            //等待连接关闭
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
