package com.mycroft.lib.net;

import android.content.Context;
import android.util.ArrayMap;

import com.blankj.utilcode.util.LogUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private static RemoteService sRemoteService;

    /**
     * 进行初始化
     *
     * @param context 上下文
     * @param maker   {@link OkHttpClient}构造器
     */
    public static void init(@NonNull Context context, @Nullable OkHttpClientMaker maker) {
        if (sRemoteService == null) {
            synchronized (RemoteService.class) {
                if (sRemoteService == null) {
                    sRemoteService = new RemoteService(context.getApplicationContext(), maker);
                }
            }
        }
    }

    public static RemoteService getImpl() {
        if (sRemoteService == null) {
            throw new IllegalStateException("RemoteService has not been initialized!");
        }
        return sRemoteService;
    }

    private final Context mAppContext;
    private final OkHttpClient mHttpClient;

    private ArrayMap<String, Retrofit> mRetrofitMap = new ArrayMap<>();

    private RemoteService(Context appContext, @Nullable OkHttpClientMaker maker) {
        mAppContext = appContext;
        if (maker == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(LogUtils::d);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            mHttpClient = new OkHttpClient.Builder()
                    .cache(new Cache(new File(mAppContext.getCacheDir(), "net"), 10 << 20))
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .addNetworkInterceptor(loggingInterceptor)
                    .build();
        } else {
            mHttpClient = maker.makeOkHttpClient();
        }
    }

    /**
     * 构造{@link Retrofit}
     *
     * @param baseUrl 构造{@link Retrofit}时的base url
     */
    public void initRetrofit(@NonNull String baseUrl, @Nullable RetrofitMaker maker) {
        if (mRetrofitMap.containsKey(baseUrl)) {
            return;
        }
        final Retrofit retrofit;
        if (maker == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(mHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(StringConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mRetrofitMap.put(baseUrl, retrofit);
        } else {
            retrofit = maker.makeRetrofit(baseUrl);
        }
        mRetrofitMap.put(baseUrl, retrofit);
    }

    /**
     * 获取{@link OkHttpClient}, 可提供其他模块复用
     *
     * @return {@link OkHttpClient}
     */
    public OkHttpClient getHttpClient() {
        return mHttpClient;
    }

    /**
     * 构造 api service 接口
     *
     * @param klazz api 接口类类对象
     * @param <T>   api 接口类型
     * @return api 接口实现类
     */
    public <T> T createApiService(String baseUrl, @NonNull Class<T> klazz) {
        Retrofit retrofit = mRetrofitMap.get(baseUrl);
        if (retrofit != null) {
            return retrofit.create(klazz);
        } else {
            throw new IllegalStateException("It hasn't been a Retrofit to handle '" + baseUrl + "'.");
        }
    }

}
