package com.mycroft.lib.util;

import com.mycroft.lib.net.HttpConstants;

/**
 * Created by Mycroft on 2016/3/31.
 */
public final class HttpUtil {
    public static String cacheControl(boolean forceCache) {
        return forceCache ? HttpConstants.FORCE_CONTROL : null;
    }
}
