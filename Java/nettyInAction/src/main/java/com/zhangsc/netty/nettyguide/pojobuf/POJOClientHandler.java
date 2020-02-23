package com.zhangsc.netty.nettyguide.pojobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName POJOClientHandler
 * @Description: TODO
 * @Author Zhangsc
 * @Date 2020/1/22
 * @Version V1.0
 **/
@Slf4j
public class POJOClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        UnixTime unixTime = (UnixTime) message;
        log.info(unixTime.toString());
        context.close();
    }
}
