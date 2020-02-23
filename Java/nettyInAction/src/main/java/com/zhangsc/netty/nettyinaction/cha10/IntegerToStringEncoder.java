package com.zhangsc.netty.nettyinaction.cha10;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @ClassName IntegerToStringEncoder  ✺
 * @Description ✻ 代码清单10-6 IntegerToStringEncoder类
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 15:19 ✾
 * @Version 1.0.0 ✵
 **/
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
