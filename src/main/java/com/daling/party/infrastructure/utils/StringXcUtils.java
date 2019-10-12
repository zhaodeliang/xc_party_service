package com.daling.party.infrastructure.utils;

/**
 * @author jiwei.xue
 * @date 2019/4/28 15:23
 */
public class StringXcUtils {

    /**
     * 隐藏手机号中间4位 用*代替
     * @param mobile
     * @return
     */
    public static String hidePhoneNum(String mobile) {
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }
}
