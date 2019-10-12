package com.daling.party.infrastructure.utils;

import com.daling.party.infrastructure.exception.GenericBusinessException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by lilindong on 2019/4/8.
 */
public class AssertUtil {

    private AssertUtil() {}

    public static void isTrue(boolean expression, String message) {
        if (expression) {
            throw new GenericBusinessException(message);
        }
    }


    public static void notNull(Object object, String message) {
        if (Objects.isNull(object)) {
            throw new GenericBusinessException(message);
        }
    }
    public static void notNull(Object object) {
        notNull(object,"参数不能为空");
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new GenericBusinessException(message);
        }
    }

    public static void notEmpty(String str, String message) {
        if (StringUtils.isEmpty(str)) {
            throw new GenericBusinessException(message);
        }
    }
}
