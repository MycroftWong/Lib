package com.mycroft.lib.util;

/**
 * Created by Mycroft on 2016/4/11.
 */
public final class CheckUtil {

    private CheckUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void checkTrue(boolean flag, String description) {
        if (!flag) {
            throw new RuntimeException(description);
        }
    }
}
