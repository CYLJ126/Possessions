package com.zhangsc.netty.nettyinaction.cha13;

import java.net.InetSocketAddress;

/**
 * @ClassName LogEvent  ✺
 * @Description ✻ 代码清单13-1 LogEvent消息 POJO
 * @Author zhangsc ≧◔◡◔≦
 * @Date 2020/2/9 16:56 ✾
 * @Version 1.0.0 ✵
 **/
public class LogEvent {
    public static final byte SEPARATOR = (byte) ':';

    //发送LogEvent 的源的InetSocketAddress
    private final InetSocketAddress source;
    //所发送的LogEvent的日志文件的名称
    private final String logfile;
    //消息内容
    private final String msg;
    //接收LogEvent的时间
    private final long received;

    public LogEvent(String logfile, String msg) {
        this(null, -1, logfile, msg);
    }

    public LogEvent(InetSocketAddress source, long received,
                    String logfile, String msg) {
        this.source = source;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }

    public InetSocketAddress getSource() {
        return source;
    }

    public String getLogfile() {
        return logfile;
    }

    public String getMsg() {
        return msg;
    }

    public long getReceivedTimestamp() {
        return received;
    }
}
