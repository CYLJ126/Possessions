package com.zhangsc.netty.nettyinaction.cha11;

import io.netty.channel.ChannelInitializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @ClassName HttpsCodecInitializer  ✺
 * @Description ✻ 代码清单11-5 使用HTTPS
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 17:23 ✾
 * @Version 1.0.0 ✵
 **/
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean client;

    public HttpsCodecInitializer(SslContext context, boolean isClient) {
        this.context = context;
        this.client = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine engine = context.newEngine(ch.alloc());
        //将SslHandler添加到ChannelPipeline中以使用HTTPS
        pipeline.addFirst("ssl", new SslHandler(engine));
        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            pipeline.addLast("codec", new HttpServerCodec());
        }
    }
}
