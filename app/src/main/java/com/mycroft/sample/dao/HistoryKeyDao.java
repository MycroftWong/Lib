package com.mycroft.sample.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mycroft.sample.model.HistoryKey;

import java.util.List;

import io.reactivex.Flowable;

/**
 * 搜索历史dao
 *
 * @author wangqiang
 */
@Dao
public interface HistoryKeyDao {

    @Query("SELECT * FROM history_key ORDER BY date DESC LIMIT 10")
    Flowable<List<HistoryKey>> getAllHistoryKey();

    @Insert
    Long add(HistoryKey historyKey);

    @Delete
    int deleteAll(List<HistoryKey> historyKeys);

    @Query("SELECT * FROM history_key WHERE `key`=:key")
    HistoryKey getHistoryKey(String key);

    @Update
    int updateHistoryKey(HistoryKey historyKey);

    @Delete
    int delete(HistoryKey historyKey);
}
