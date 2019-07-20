package com.mycroft.sample.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class HistorySearchAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public HistorySearchAdapter(@Nullable List<String> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
