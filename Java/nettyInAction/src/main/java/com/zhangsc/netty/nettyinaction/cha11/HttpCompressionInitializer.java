package com.zhangsc.netty.nettyinaction.cha11;

import io.netty.channel.ChannelInitializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;
import sun.plugin2.gluegen.runtime.CPU;

/**
 * @ClassName HttpCompressionInitializer  ✺
 * @Description ✻ 代码清单11-4 自动压缩HTTP消息
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 17:18 ✾
 * @Version 1.0.0 ✵
 **/
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpCompressionInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());
            //如果是客户端，则添加HttpContentDecompressor以处理来自服务器的压缩内容
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        } else {
            pipeline.addLast("codec", new HttpServerCodec());
            //如果是服务器，则添加HttpContentCompressor来压缩数据（如果客户端支持它）
            pipeline.addLast("compressor", new HttpContentCompressor());
        }
    }
}
