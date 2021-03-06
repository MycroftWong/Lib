package com.mycroft.sample.activity;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mycroft.lib.util.DisposableUtil;
import com.mycroft.sample.R;
import com.mycroft.sample.common.CommonActivity;
import com.mycroft.sample.component.WebService;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author mycroft
 */
public class SplashActivity extends CommonActivity {

    @Override
    protected int getResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initFields(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initViews() {

    }

    private Disposable disposable;

    @Override
    protected void loadData() {
        disposable = Observable.just(System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .map(startTime -> {

                    SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context));
                    SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context));

                    WebService.start(this);
                    TimeUnit.MILLISECONDS.sleep(TIME_SPLASH - (System.currentTimeMillis() - startTime));
                    return startTime;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<Long, ObservableSource<Boolean>>) aLong ->
                        new RxPermissions(SplashActivity.this)
                                .request(Manifest.permission.ACCESS_NETWORK_STATE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .subscribe(granted -> {
                            if (granted) {
                                startActivity(MainActivity.Companion.getIntent(this));
                            }
                            finish();
                        },
                        throwable -> finish());
    }

    @Override
    protected void onDestroy() {
        DisposableUtil.dispose(disposable);
        disposable = null;
        super.onDestroy();
    }

    private static final int TIME_SPLASH = 3000;
}
