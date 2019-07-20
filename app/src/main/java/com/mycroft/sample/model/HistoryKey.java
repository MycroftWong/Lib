package com.mycroft.sample.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * 历史搜索关键词
 *
 * @author wangqiang
 */
@Entity(tableName = "history_key", indices = {@Index(value = "key", name = "index_history_key", unique = true)})
public class HistoryKey {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "key")
    public String key;

    @ColumnInfo(name = "date")
    public long date;
}
