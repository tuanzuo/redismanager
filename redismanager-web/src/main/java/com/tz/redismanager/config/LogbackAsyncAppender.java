package com.tz.redismanager.config;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AsyncAppenderBase;

/**
 * 自定义Logback的异步日志Appender
 *
 * @author tuanzuo
 * @version 1.7.2
 * @time 2022-09-11 13:50
 **/
public class LogbackAsyncAppender extends AsyncAppender {

    public LogbackAsyncAppender() {
        super();
        //设置异步日志队列的大小为500，默认256
        this.setQueueSize(500);
        //设置日志丢弃的阀值为50，默认为-1(不丢弃)
        this.setDiscardingThreshold(50);
        /**
         * 设置日志数据入队时是否阻塞，默认为false(阻塞)
         * true:表示入队时队列满了直接丢弃不等待(对性能会有很大提升)，false:表示入队时队列满了会进行阻塞等待
         * @see AsyncAppenderBase#put(java.lang.Object)
         */
        this.setNeverBlock(true);
        /**
         * 设置日志是否包含调用栈数据，默认为false(不包含)
         * @see AsyncAppender#preprocess(ch.qos.logback.classic.spi.ILoggingEvent)
         */
        this.setIncludeCallerData(true);
    }

    /**
     * 当日志的队列容量小于discardingThreshold时，丢弃TRACE, DEBUG日志，保留日志Level大于等于INFO的日志
     *
     * @param event
     * @return true:丢弃日志，false:不丢弃日志
     * @see AsyncAppenderBase#isQueueBelowDiscardingThreshold()
     */
    @Override
    protected boolean isDiscardable(ILoggingEvent event) {
        Level level = event.getLevel();
        return level.toInt() < Level.INFO_INT;
    }

}
