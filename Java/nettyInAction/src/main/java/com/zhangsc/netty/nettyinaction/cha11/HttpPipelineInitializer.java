package com.zhangsc.netty.nettyinaction.cha11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @ClassName HttpPipelineInitializer  ✺
 * @Description ✻ 代码清单11-2 添加HTTP支持
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 17:02 ✾
 * @Version 1.0.0 ✵
 **/
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            //如果是客户端，则添加HttpResponseDecoder以处理来自服务器的响应，添加HttpRequestEncoder以向服务器发送请求
            pipeline.addLast("decoder", new HttpResponseDecoder());
            pipeline.addLast("encoder", new HttpRequestEncoder());
        } else {
            //如果是服务器，则添加HttpRequestDecoder以接收来自客户端的请求，添加HttpResponseEncoder以向客户端发送响应
            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("encoder", new HttpResponseEncoder());
        }
    }
}
