package com.zhangsc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.http.websocketx.WebSocket08FrameEncoder;

import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        removeLine();
//        testByteBuf();
    }

    /**
     * Netty in Action
     */
    private static void removeLine() {
        String str = "冝僴..\uE777\uE019м..╊だ.\uE6CE.\uE66F.\uE6BC.甶\uE019.ン\uE4CC\n" +
                "冝僴睰\uE661ぞ\uE70B\uE4C7\uE64E磷\uE711蘕审\uE4CC\n" +
                "冝僴.\uE085.狠.\uE095\uE5E6..稀\uE019API\uE4CC\n" +
                "冝僴ㄏㄤ\uE784场.︽\uE6C8HTTPS \uE5FD\uE4C9";
        System.out.println("&emsp;&emsp;" +
                str.replace("\n", "")
                        .replace(" ", "")
                        .replace("\uF06E\uF020", "\n* ")
                        .replace("\uF06E", "\n* "));
    }

    public static void testByteBuf() {
        ByteBuf buf = Unpooled.buffer();
        buf.capacity(40);
        System.out.println(buf.capacity());
        System.out.println("-----1------");
        for (int i = 0; i < 10; i++) {
            buf.writeInt(i);
        }
        System.out.println(buf.readerIndex());
        System.out.println(buf.writerIndex());
        System.out.println("-----2------");
        for (int j = 0; j < 6; j++) {
            buf.readInt();
        }
        System.out.println(buf.readerIndex());
        System.out.println(buf.writerIndex());
        System.out.println("-----3------");

        buf.writerIndex(24);
        System.out.println(buf.readerIndex());
        System.out.println(buf.writerIndex());
        System.out.println("-----4------");

        for (int i = 0; i < 10; i++) {
            buf.writeInt(i);
        }
        System.out.println(buf.readerIndex());
        System.out.println(buf.readableBytes());
        System.out.println(buf.writerIndex());
    }
}

