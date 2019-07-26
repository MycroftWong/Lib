package com.mycroft.sample.service;

import androidx.annotation.NonNull;

import com.mycroft.sample.model.HistoryKey;

import java.util.List;

import io.reactivex.Flowable;

/**
 * 历史搜索service
 *
 * @author wangqiang
 */
public interface IHistoryKeyService {

    /**
     * 添加历史搜索关键词
     *
     * @param key 搜索关键字
     */
    void addHistoryKey(@NonNull String key);

    /**
     * 清空历史搜索关键字
     */
    void clearHistoryKey();

    /**
     * 删除历史搜索关键字
     *
     * @param historyKey 历史搜索关键字
     */
    void deleteHistoryKey(@NonNull HistoryKey historyKey);

    /**
     * 获取搜索历史关键词
     *
     * @return 历史关键字列表 Flowable
     */
    Flowable<List<HistoryKey>> getAllHistoryKey();
}
