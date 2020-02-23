package com.zhangsc.netty.nettyinaction.cha12;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @ClassName SecureChatServerInitializer  ✺
 * @Description ✻ 代码清单12-6 为ChannelPipeline添加加密
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/9 15:46 ✾
 * @Version 1.0.0 ✵
 **/
public class SecureChatServerInitializer extends ChatServerInitializer {
    private final SslContext context;

    public SecureChatServerInitializer(ChannelGroup group, SslContext context) {
        super(group);
        this.context = context;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        super.initChannel(ch);
        SSLEngine engine = context.newEngine(ch.alloc());
        engine.setUseClientMode(false);
        ch.pipeline().addFirst(new SslHandler(engine));
    }
}
