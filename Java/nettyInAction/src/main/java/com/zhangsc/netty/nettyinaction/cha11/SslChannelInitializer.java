package com.zhangsc.netty.nettyinaction.cha11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @ClassName SslChannelInitializer  ✺
 * @Description ✻ 代码清单11-1 添加SSL/TLS支持
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 16:45 ✾
 * @Version 1.0.0 ✵
 **/
public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean startTls;

    public SslChannelInitializer(SslContext context, boolean startTls) {
        //传入要使用的SslContexet
        this.context = context;
        //如果被设置为true，第一个写入的消息将不会被加密（客户端应该设置为true）
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        //对于每个SslHandler实例都使用Channel的ByteBufAllocator从SslContext获取一个新的SSLEngine
        SSLEngine engine = context.newEngine(ch.alloc());
        //将SslHandler作为第一个ChannelHandler添加到ChannelPipeline中
        ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }
}
