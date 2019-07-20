package com.mycroft.sample.dao;

import android.content.Context;

import androidx.annotation.NonNull;

import com.mycroft.sample.model.HistoryKey;
import com.mycroft.sample.service.BackgroundService;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class HistoryKeyService {

    public static HistoryKeyService getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final HistoryKeyService INSTANCE = new HistoryKeyService();
    }

    public static void addHistoryKey(@NonNull Context context, @NonNull String key) {
        BackgroundService.addHistoryKey(context, key);
    }

    public static void deleteHistoryKey(@NonNull Context context) {
        BackgroundService.deleteHistoryKey(context);
    }

    private final HistoryKeyDao historyKeyDao;

    private HistoryKeyService() {
        AppDatabase appDatabase = AppDatabase.getInstance();
        historyKeyDao = appDatabase.historyKeyDao();
    }

    public Flowable<Long> addHistory(String key) {
        HistoryKey historyKey = new HistoryKey();
        historyKey.key = key;
        historyKey.date = System.currentTimeMillis();

        HistoryKey queried = historyKeyDao.getHistoryKey(key);
        if (queried == null) {
            return Flowable.just(historyKeyDao.add(historyKey));
        } else {
            historyKey.id = queried.id;
            return Flowable.just((long) historyKeyDao.updateHistoryKey(historyKey));
        }

//        return historyKeyDao.add(historyKey)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .flatMap(new Function<Long, Publisher<Long>>() {
//                    @Override
//                    public Publisher<Long> apply(Long aLong) throws Throwable {
//                        if (aLong > 0) {
//                            return Flowable.just(aLong);
//                        } else {
//                            return historyKeyDao.update;
//                        }
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Long> clearHistory() {
        return historyKeyDao.getAllHistoryKey()
                .map(historyKeys -> Long.valueOf(historyKeyDao.deleteAll(historyKeys)));
    }

    public Flowable<List<HistoryKey>> getAllHistoryKey() {
        return historyKeyDao.getAllHistoryKey()
                .compose(async());
    }

    private static <T> FlowableTransformer<T, T> async() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
