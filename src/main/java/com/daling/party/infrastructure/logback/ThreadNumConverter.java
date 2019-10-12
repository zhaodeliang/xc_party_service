package com.daling.party.infrastructure.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * FileName: ThreadNumConverter
 *
 * @author: 赵得良
 * Date:     2019/4/24 0024 18:01
 * Description:
 */
public class ThreadNumConverter extends ClassicConverter {
    /**
     * 当需要显示线程ID的时候，返回当前调用线程的ID
     */
    @Override
    public String convert(ILoggingEvent event) {
        return String.valueOf(Thread.currentThread().getId());
    }
}
