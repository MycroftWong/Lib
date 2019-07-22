package com.mycroft.sample.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.mycroft.sample.dao.HistoryKeyService;
import com.mycroft.sample.model.HistoryKey;

/**
 * @author wangqiang
 */
public class BackgroundService extends IntentService {

    private static final String ACTION_ADD = AppUtils.getAppPackageName() + ".add.action";

    private static final String ACTION_CLEAR = AppUtils.getAppPackageName() + ".clear.action";
    private static final String ACTION_DELETE = AppUtils.getAppPackageName() + ".delete.action";

    private static final String EXTRA_HISTORY_KEY = "history_key.extra";

    public static void addHistoryKey(@NonNull Context context, @NonNull String key) {
        Intent intent = new Intent();
        intent.setPackage(context.getPackageName());
        intent.setAction(ACTION_ADD);
        intent.putExtra(EXTRA_HISTORY_KEY, key);
        context.startService(intent);
    }

    public static void clearHistoryKey(@NonNull Context context) {
        Intent intent = new Intent();
        intent.setPackage(context.getPackageName());
        intent.setAction(ACTION_CLEAR);
        context.startService(intent);
    }

    public static void deleteHistoryKey(@NonNull Context context, @NonNull HistoryKey historyKey) {
        Intent intent = new Intent();
        intent.setPackage(context.getPackageName());
        intent.setAction(ACTION_DELETE);
        intent.putExtra(EXTRA_HISTORY_KEY, historyKey);
        context.startService(intent);
    }

    public BackgroundService() {
        super(AppUtils.getAppName().concat(".background"));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ADD.equals(action)) {
                String key = intent.getStringExtra(EXTRA_HISTORY_KEY);
                HistoryKeyService.getInstance()
                        .addHistory(key)
                        .subscribe();
            } else if (ACTION_CLEAR.equals(action)) {
                HistoryKeyService.getInstance().clearHistory()
                        .subscribe();
            } else if (ACTION_DELETE.equals(action)) {
                HistoryKey historyKey = intent.getParcelableExtra(EXTRA_HISTORY_KEY);
                HistoryKeyService.getInstance()
                        .deleteHistoryKey(historyKey)
                        .subscribe();
            }
        }
    }
}
