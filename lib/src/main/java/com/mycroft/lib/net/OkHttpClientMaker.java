package com.mycroft.lib.net;

import okhttp3.OkHttpClient;

/**
 * Created by MycroftWong on 2017/1/14.
 *
 * @author mycroft
 */
public interface OkHttpClientMaker {

    /**
     * 构造OkHttpClient
     *
     * @return {@link OkHttpClient}
     */
    OkHttpClient makeOkHttpClient();
}
