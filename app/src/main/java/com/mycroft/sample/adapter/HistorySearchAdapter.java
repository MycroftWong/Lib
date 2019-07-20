package com.mycroft.sample.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mycroft.sample.model.HistoryKey;

import java.util.List;

public class HistorySearchAdapter extends BaseQuickAdapter<HistoryKey, BaseViewHolder> {
    public HistorySearchAdapter(@Nullable List<HistoryKey> data) {
        super(android.R.layout.simple_list_item_1, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryKey item) {
        helper.setText(android.R.id.text1, item.key);
    }
}
