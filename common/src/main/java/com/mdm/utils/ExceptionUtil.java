package com.mdm.utils;

import org.apache.logging.log4j.util.Strings;

import java.util.Objects;

/**
 * @author dongming.ma
 * @date 2022/6/6 22:10
 */
public class ExceptionUtil {

    public static String getCauseMsg(final Throwable t) {
        final Throwable cause = t;
        if (Objects.isNull(cause)) {
            return Strings.EMPTY;
        }
        return cause.getMessage();
    }
}
