package com.mycroft.sample.component;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;

/**
 * 和{@link com.mycroft.sample.activity.WebViewActivity}在同一进程，用于提前启动进程，加快第一次访问速度
 *
 * @author mycroft
 */
public final class WebService extends Service {

    public static void start(@NonNull Context context) {
        context.startService(new Intent(context, WebService.class));
    }

    public WebService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
