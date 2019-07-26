package com.mycroft.sample.service;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;
import com.mycroft.sample.component.BackgroundService;
import com.mycroft.sample.dao.AppDatabase;
import com.mycroft.sample.dao.HistoryKeyDao;
import com.mycroft.sample.model.HistoryKey;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 搜索历史 service实现类
 *
 * @author wangqiang
 */
public final class HistoryKeyServiceImpl implements IHistoryKeyService {

    private static class Holder {
        private static final HistoryKeyServiceImpl INSTANCE = new HistoryKeyServiceImpl();
    }

    public static HistoryKeyServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private final HistoryKeyDao historyKeyDao;

    private HistoryKeyServiceImpl() {
        AppDatabase appDatabase = AppDatabase.getInstance();
        historyKeyDao = appDatabase.historyKeyDao();
    }

    @Override
    public void addHistoryKey(@NonNull String key) {
        BackgroundService.addHistoryKey(Utils.getApp(), key);
    }

    @Override
    public void clearHistoryKey() {
        BackgroundService.clearHistoryKey(Utils.getApp());
    }

    @Override
    public void deleteHistoryKey(@NonNull HistoryKey historyKey) {
        BackgroundService.deleteHistoryKey(Utils.getApp(), historyKey);
    }

    @Override
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
