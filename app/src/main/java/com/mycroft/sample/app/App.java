package com.mycroft.sample.app;

import android.app.Application;

import com.mycroft.lib.view.DefaultLoadingAdapter;
import com.mycroft.lib.view.Loading;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Loading.initDefault(new DefaultLoadingAdapter());
    }
}
