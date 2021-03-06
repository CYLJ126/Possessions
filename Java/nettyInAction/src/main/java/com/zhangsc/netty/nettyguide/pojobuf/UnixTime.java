package com.zhangsc.netty.nettyguide.pojobuf;

import java.util.Date;

/**
 * @ClassName UnixTime
 * @Description: 时间类POJO
 * @Author Zhangsc
 * @Date 2020/1/22
 * @Version V1.0
 **/
public class UnixTime {
    private final long value;

    public UnixTime() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public UnixTime(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return new Date((value() - 2208988800L) * 1000L).toString();
    }
}
