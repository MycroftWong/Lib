package com.mycroft.sample.net;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mycroft.lib.net.RemoteService;
import com.mycroft.lib.net.StringConverterFactory;
import com.mycroft.sample.exception.NetDataException;
import com.mycroft.sample.model.ArticleListModel;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetService {

    public static NetService getInstance() {
        return Holder.netService;
    }

    private static class Holder {
        private static NetService netService = new NetService();
    }

    private static final String BASE_URL = "https://www.wanandroid.com/";

    private NetService() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(LogUtils::w);
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(new Cache(new File(Utils.getApp().getCacheDir(), "net"), 10 << 20))
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        RemoteService.init(Utils.getApp(), () -> httpClient);
        RemoteService.getImpl().initRetrofit(BASE_URL, baseUrl -> new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build());

        service = RemoteService.getImpl().createApiService(BASE_URL, IApiService.class);
    }

    private final IApiService service;

    public Observable<ArticleListModel> getArticleList(int page) {
        return service.getArticleList(page)
                .compose(async())
                .map(articleListModelNetModel -> {
                    if (articleListModelNetModel.getErrorCode() == 0) {
                        return articleListModelNetModel.getData();
                    } else {
                        throw NetDataException.newInstance(articleListModelNetModel.getErrorMsg());
                    }
                });
    }

    /**
     * 线程切换
     *
     * @param <T>
     * @return
     */
    private static <T> ObservableTransformer<T, T> async() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
