package com.zhangsc.netty.nettyguide.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName SimpleChatClientHandler  ✺
 * @Description ✻ 客户端的处理类比较简单，只需要将读到的信息打印出来即可
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/1/29 19:13 ✾
 * @Version 1.0.0 ✵
 **/
@Slf4j
public class SimpleChatClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        log.info(msg);
    }
}
