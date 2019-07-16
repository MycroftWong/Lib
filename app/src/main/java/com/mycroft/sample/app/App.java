package com.mycroft.sample.app;

import android.app.Application;

import com.billy.android.loading.Gloading;
import com.mycroft.lib.view.GloadingAdapter;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Gloading.initDefault(new GloadingAdapter());

    }
}
