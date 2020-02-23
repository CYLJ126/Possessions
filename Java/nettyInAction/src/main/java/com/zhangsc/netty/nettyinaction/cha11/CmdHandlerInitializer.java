package com.zhangsc.netty.nettyinaction.cha11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.util.Objects;

/**
 * @ClassName CmdHandlerInitializer  ✺
 * @Description ✻ 代码清单11-9 使用ChannelInitializer安装解码器
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/8 19:48 ✾
 * @Version 1.0.0 ✵
 **/
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {
    static final byte SPACE = (byte) ' ';

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //添加CmdDecoder以提取Cmd对象，并将它转发给下一个ChannelInboundHandler
        pipeline.addLast(new CmdDecoder(64 * 1024));
        //添加CmdHandler以接收和处理Cmd对象
        pipeline.addLast(new CmdHandler());
    }

    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
            //Do something with the command
            //处理传经ChannelPipeline的Cmd对象
        }
    }

    public static final class CmdDecoder extends LineBasedFrameDecoder {

        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext context, ByteBuf buffer) throws Exception {
            //从ByteBuf中提取由行尾符序列分隔的帧
            ByteBuf frame = (ByteBuf) super.decode(context, buffer);
            if (frame == null) {
                //如果输入中没有帧，则返回null
                return null;
            }
            //查找第一个空格字符的索引，前面是命令名称，后面是参数
            int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
            //使用包含有命令名称和参数的切片创建新的的Cmd对象
            return new Cmd(frame.slice(frame.readerIndex(), index), frame.slice(index + 1, frame.writerIndex()));
        }
    }

    /**
     * POJO
     */
    public static final class Cmd {
        private final ByteBuf name;
        private final ByteBuf args;

        public Cmd(ByteBuf name, ByteBuf args) {
            this.name = name;
            this.args = args;
        }

        public ByteBuf name() {
            return name;
        }

        public ByteBuf args() {
            return args;
        }
    }
}
