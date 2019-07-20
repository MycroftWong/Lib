package com.mycroft.lib.net;

import android.content.Context;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 简化{@link Retrofit}的构造工作
 * <p>
 * Created by Mycroft on 2016/4/18.
 *
 * @author mycroft
 */
public final class RemoteService {

    private static RemoteService remoteService;

    /**
     * 进行初始化
     *
     * @param context 上下文
     * @param maker   {@link OkHttpClient}构造器
     */
    public static void init(@NonNull Context context, @Nullable OkHttpClientMaker maker) {
        if (remoteService == null) {
            synchronized (RemoteService.class) {
                if (remoteService == null) {
                    remoteService = new RemoteService(context.getApplicationContext(), maker);
                }
            }
        }
    }

    public static RemoteService getImpl() {
        if (remoteService == null) {
            throw new IllegalStateException("RemoteService has not been initialized!");
        }
        return remoteService;
    }

    private final OkHttpClient httpClient;

    private ArrayMap<String, Retrofit> retrofitMap = new ArrayMap<>();

    private RemoteService(Context appContext, @Nullable OkHttpClientMaker maker) {
        if (maker == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(LogUtils::d);
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

            httpClient = new OkHttpClient.Builder()
                    .cache(new Cache(new File(appContext.getCacheDir(), "net"), 10 << 20))
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .addNetworkInterceptor(loggingInterceptor)
                    .build();
        } else {
            httpClient = maker.makeOkHttpClient();
        }
    }

    /**
     * 构造{@link Retrofit}
     *
     * @param baseUrl 构造{@link Retrofit}时的base url
     */
    public void initRetrofit(@NonNull String baseUrl, @Nullable RetrofitMaker maker) {
        if (retrofitMap.containsKey(baseUrl)) {
            return;
        }
        final Retrofit retrofit;
        if (maker == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(StringConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } else {
            retrofit = maker.makeRetrofit(baseUrl);
        }
        retrofitMap.put(baseUrl, retrofit);
    }

    /**
     * 获取{@link OkHttpClient}, 可提供其他模块复用
     *
     * @return {@link OkHttpClient}
     */
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 构造 api service 接口
     *
     * @param klazz api 接口类类对象
     * @param <T>   api 接口类型
     * @return api 接口实现类
     */
    public <T> T createApiService(String baseUrl, @NonNull Class<T> klazz) {
        Retrofit retrofit = retrofitMap.get(baseUrl);
        if (retrofit != null) {
            return retrofit.create(klazz);
        } else {
            throw new IllegalStateException("It hasn't been a Retrofit to handle '" + baseUrl + "'.");
        }
    }

}
