package com.mycroft.sample.net;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mycroft.lib.net.RemoteService;
import com.mycroft.lib.net.StringConverterFactory;
import com.mycroft.lib.net.cookiejar.PersistentCookieJar;
import com.mycroft.lib.net.cookiejar.cache.SetCookieCache;
import com.mycroft.lib.net.cookiejar.persistence.SharedPrefsCookiePersistor;
import com.mycroft.sample.exception.NetDataException;
import com.mycroft.sample.model.Article;
import com.mycroft.sample.model.Category;
import com.mycroft.sample.model.HotKey;
import com.mycroft.sample.model.ListData;
import com.mycroft.sample.model.OfficialAccount;
import com.mycroft.sample.model.Project;
import com.mycroft.sample.model.Tools;
import com.mycroft.sample.service.FileServiceImpl;
import com.mycroft.sample.service.IFileService;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CookieJar;
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

        IFileService fileService = FileServiceImpl.getInstance();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(LogUtils::w);
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        CookieJar cookieJar = new PersistentCookieJar(
                new SetCookieCache(),
                new SharedPrefsCookiePersistor(Utils.getApp()));

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .cache(new Cache(fileService.getNetCacheDir(), 10 << 20))
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(new CacheInterceptor())
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

    public Observable<ListData<Article>> getHomeArticleList(int page) {
        Observable<NetModel<ListData<Article>>> observable = service.getHomeArticleList(page);
        return handleResult(observable);
    }

    public Observable<ListData<Article>> getArticleList(String url, int page) {
        Observable<NetModel<ListData<Article>>> observable = service.getArticleList(String.format(Locale.US, url, page));
        return handleResult(observable);
    }

    public Observable<List<Category>> getCategoryList() {
        Observable<NetModel<List<Category>>> observable = service.getCategoryList();
        return handleResult(observable);
    }

    public Observable<List<OfficialAccount>> getOfficialAccountList() {
        Observable<NetModel<List<OfficialAccount>>> observable = service.getOfficialAccountList();
        return handleResult(observable);
    }

    public Observable<List<Tools>> getToolList() {
        Observable<NetModel<List<Tools>>> observable = service.getToolList();
        return handleResult(observable);
    }

    public Observable<List<Project>> getProjectList() {
        Observable<NetModel<List<Project>>> observable = service.getProjectList();
        return handleResult(observable);
    }

    public Observable<List<HotKey>> getHotKeyList() {
        Observable<NetModel<List<HotKey>>> observable = service.getHotKeyList();
        return handleResult(observable);
    }

    public Observable<ListData<Article>> search(String key, int page) {
        Observable<NetModel<ListData<Article>>> observable = service.search(key, page);
        return handleResult(observable);
    }

    private static <T> Observable<T> handleResult(Observable<NetModel<T>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(articleListModelNetModel -> {
                    if (articleListModelNetModel.getErrorCode() == 0) {
                        return articleListModelNetModel.getData();
                    } else {
                        throw NetDataException.newInstance(articleListModelNetModel.getErrorMsg());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
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
