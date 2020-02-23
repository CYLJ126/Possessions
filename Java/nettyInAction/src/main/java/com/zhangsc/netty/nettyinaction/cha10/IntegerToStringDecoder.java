package com.zhangsc.netty.nettyinaction.cha10;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @ClassName IntegerToStringDecoder  ✺
 * @Description ✻ 代码清单10-3 IntegerToStringDecoder类
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 14:54 ✾
 * @Version 1.0.0 ✵
 **/
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
