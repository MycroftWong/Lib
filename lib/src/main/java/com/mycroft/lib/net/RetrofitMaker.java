package com.mycroft.lib.net;

import retrofit2.Retrofit;

/**
 * 自定义{@link Retrofit}
 *
 * @author mycroft
 */
public interface RetrofitMaker {

    Retrofit makeRetrofit(String baseUrl);
}
