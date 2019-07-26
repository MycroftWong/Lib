package com.mycroft.sample.component;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.mycroft.sample.dao.AppDatabase;
import com.mycroft.sample.dao.HistoryKeyDao;
import com.mycroft.sample.model.HistoryKey;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;

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

    private HistoryKeyDao historyKeyDao;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            AppDatabase appDatabase = AppDatabase.getInstance();
            historyKeyDao = appDatabase.historyKeyDao();

            final String action = intent.getAction();
            if (ACTION_ADD.equals(action)) {
                String key = intent.getStringExtra(EXTRA_HISTORY_KEY);
                addHistory(key);
            } else if (ACTION_CLEAR.equals(action)) {
                clearHistory();
            } else if (ACTION_DELETE.equals(action)) {
                HistoryKey historyKey = intent.getParcelableExtra(EXTRA_HISTORY_KEY);
                deleteHistoryKey(historyKey);
            }
        }
    }

    /**
     * 添加搜索关键字
     *
     * @param key 搜索关键字
     */
    private void addHistory(String key) {
        HistoryKey historyKey = new HistoryKey();
        historyKey.key = key;
        historyKey.date = System.currentTimeMillis();

        HistoryKey queried = historyKeyDao.getHistoryKey(key);
        Flowable<Long> flowable;
        if (queried == null) {
            flowable = Flowable.just(historyKeyDao.add(historyKey));
        } else {
            historyKey.id = queried.id;
            flowable = Flowable.just((long) historyKeyDao.updateHistoryKey(historyKey));
        }
        flowable.subscribe(INSTANCE);
    }

    /**
     * 清空搜索历史关键字
     */
    private void clearHistory() {
        historyKeyDao.getAllHistoryKey()
                .map(historyKeys -> (long) historyKeyDao.deleteAll(historyKeys))
                .subscribe(INSTANCE);
    }

    /**
     * 删除历史关键字
     *
     * @param historyKey 历史关键字
     */
    private void deleteHistoryKey(HistoryKey historyKey) {
        Flowable.just(historyKeyDao.delete(historyKey))
                .map(Long::valueOf)
                .subscribe(INSTANCE);
    }

    /**
     * 处理异常等信息的空Subscriber
     */
    private static final Subscriber<Long> INSTANCE = new Subscriber<Long>() {

        @Override
        public void onSubscribe(Subscription s) {

        }

        @Override
        public void onNext(Long aLong) {

        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {

        }
    };
}
