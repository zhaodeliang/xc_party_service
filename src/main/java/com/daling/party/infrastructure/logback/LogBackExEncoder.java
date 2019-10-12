package com.daling.party.infrastructure.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

/**
 * FileName: LogBackExEncoder
 *
 * @author: 赵得良
 * Date:     2019/4/24 0024 18:00
 * Description:
 */
public class LogBackExEncoder extends PatternLayoutEncoder {
    static {
        PatternLayout.defaultConverterMap.put("T", ThreadNumConverter.class.getName());
    }
}
