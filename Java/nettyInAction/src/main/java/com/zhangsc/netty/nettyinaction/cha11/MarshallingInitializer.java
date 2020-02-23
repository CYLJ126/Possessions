package com.zhangsc.netty.nettyinaction.cha11;

import io.netty.channel.*;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import java.io.Serializable;

/**
 * @ClassName MarshallingInitializer  ✺
 * @Description ✻ 代码清单11-13 使用JBoss Marshalling
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 22:15 ✾
 * @Version 1.0.0 ✵
 **/
public class MarshallingInitializer extends ChannelInitializer<Channel> {
    private final MarshallerProvider marshallerProvider;
    private final UnmarshallerProvider unmarshallerProvider;

    public MarshallingInitializer(MarshallerProvider marshallerProvider, UnmarshallerProvider unmarshallerProvider) {
        this.marshallerProvider = marshallerProvider;
        this.unmarshallerProvider = unmarshallerProvider;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //添加MarshallingDecoder以将ByteBuf转换为POJO
        pipeline.addLast(new MarshallingDecoder(unmarshallerProvider));
        //添加MarshallingEncoder以将POJO转换为ByteBuf
        pipeline.addLast(new MarshallingEncoder(marshallerProvider));
        //添加ObjectHanlder，以处理普通的实现了Serializable接口的POJO
        pipeline.addLast(new ObjectHanlder());
    }

    public static final class ObjectHanlder extends SimpleChannelInboundHandler<Serializable> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) throws Exception {
            //Do something
        }
    }
}
