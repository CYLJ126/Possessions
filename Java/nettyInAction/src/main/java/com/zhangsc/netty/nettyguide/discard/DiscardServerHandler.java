package com.zhangsc.netty.nettyguide.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName DiscardServerHandler
 * @Description: 处理服务端 channel
 * @Author Zhangsc
 * @Date 2020/1/21
 * @Version V1.0
 **/
@Slf4j
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 1.channelActive() 方法将会在 连接被建立 并且 准备进行通信时 被调用。因此
     * 让我们在这个方法里完成一个代表当前时间的32位整数消息的构建工作。
     */
    @Override
    public void channelActive(final ChannelHandlerContext context) {
        /**
         * 2.为了发送一个新的消息，我们需要分配一个包含这个消息的新的缓冲。因为
         * 我们需要写入一个32位的整数，因此我们需要一个至少有4个字节的 ByteBuf。
         * 通过 ChannelHandlerContext.alloc() 得到一个当前的ByteBufAllocator，
         * 然后分配一个新的缓冲。
         */
        final ByteBuf time = context.alloc().buffer(4);
        //2208988800L Mon Jan 26 21:36:28 CST 1970
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        /**
         * 3.和往常一样我们需要编写一个构建好的消息。但是等一等，flip 在哪？难道
         * 我们使用 NIO 发送消息时不是调用 java.nio.ByteBuffer.flip() 吗？
         * ByteBuf 之所以没有这个方法因为有两个指针，一个对应读操作一个对应写操
         * 作。当你向 ByteBuf 里写入数据的时候写指针的索引就会增加，同时读指针
         * 的索引没有变化。读指针索引和写指针索引分别代表了消息的开始和结束。
         *
         * 比较起来，NIO 缓冲并没有提供一种简洁的方式来计算出消息内容的开始和
         * 结尾，除非你调用 flip 方法。当你忘记调用 flip 方法而引起没有数据
         * 或者错误数据被发送时，你会陷入困境。这样的一个错误不会发生在
         * Netty 上，因为我们对于不同的操作类型有不同的指针。你会发现这样
         * 的使用方法会让你过程变得更加的容易，因为你已经习惯一种没有使用
         * flip 的方式。
         *
         * 另外一个点需要注意的是 ChannelHandlerContext.write() (和
         * writeAndFlush() )方法会返回一个 ChannelFuture 对象，一个
         * ChannelFuture 代表了一个还没有发生的 I/O 操作。这意味着任何一
         * 个请求操作都不会马上被执行，因为在 Netty 里所有的操作都是异步
         * 的。举个例子下面的代码中在消息被发送之前可能会先关闭连接。
         * </p>
         * Channel ch = ...;
         * ch.writeAndFlush(message);
         * ch.close();
         * </p>
         * 因此你需要在 write() 方法返回的 ChannelFuture 完成后调用
         * close() 方法，然后当它的写操作已经完成他会通知他的监听者。
         * 请注意, close() 方法也可能不会立马关闭，它也会返回一个
         * ChannelFuture。
         */
        final ChannelFuture future = context.writeAndFlush(time);
        /**
         * 4.当一个写请求已经完成是如何通知到我们？这个只需要简单地在
         * 返回的 ChannelFuture 上增加一个ChannelFutureListener。这
         * 里我们构建了一个匿名的 ChannelFutureListener 类用来在操
         * 作完成时关闭 Channel。
         * 或者，你可以使用简单的预定义监听器代码:
         *  f.addListener(ChannelFutureListener.CLOSE);
         */
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                assert future == f;
                context.close();
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        /**
         * 1. ChannelHandlerContext 对象提供了许多操作，使你能够触发各种各样的 I/O
         * 事件和操作。这里我们调用了 write(Object) 方法来逐字地把接受到的消息写入。
         * 请注意不同于 DISCARD 的例子我们并没有释放接受到的消息，这是因为当写入的
         * 时候 Netty 已经帮我们释放了。
         */
        context.write(message);
        /**
         * 2. ctx.write(Object) 方法不会使消息写入到通道上，它被缓冲在了内部，你
         * 需要调用 ctx.flush() 方法来把缓冲区中数据强行输出。或者你可以用更简洁
         * 的 cxt.writeAndFlush(msg) 以达到同样的目的。
         */
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        context.close();
    }
}
