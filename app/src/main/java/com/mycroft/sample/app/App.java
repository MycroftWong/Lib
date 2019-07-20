package com.mycroft.sample.app;

import android.app.Application;

import com.mycroft.lib.view.Loading;
import com.mycroft.sample.view.SpinLoadingAdapter;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Loading.initDefault(new SpinLoadingAdapter());
    }
}
