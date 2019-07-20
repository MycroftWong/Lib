package com.mycroft.sample.net;

import androidx.annotation.NonNull;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.NetworkUtils;
import com.mycroft.lib.net.HttpConstants;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author wangqiang
 */
public class CacheInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        boolean isNetworkConnected = NetworkUtils.isConnected();

        // 没网强制从缓存读取(必须得写，不然断网状态下，退出应用，或者等待一分钟后，就获取不到缓存）
        if (!isNetworkConnected) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (isNetworkConnected) {
            // 有网失效一分钟
            int maxAge = TimeConstants.MIN;
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader(HttpConstants.CACHE_CONTROL)
                    .header(HttpConstants.CACHE_CONTROL, "public, max-age=" + maxAge)
                    .build();
        } else {
            // 没网失效一天
            int maxStale = TimeConstants.DAY;
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader(HttpConstants.CACHE_CONTROL)
                    .header(HttpConstants.CACHE_CONTROL, "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;
    }
}
