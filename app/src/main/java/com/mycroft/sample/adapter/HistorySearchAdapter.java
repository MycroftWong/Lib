package com.mycroft.sample.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mycroft.sample.R;
import com.mycroft.sample.model.HistoryKey;

import java.util.List;

/**
 * @author wangqiang
 */
public class HistorySearchAdapter extends BaseQuickAdapter<HistoryKey, BaseViewHolder> {
    public HistorySearchAdapter(@Nullable List<HistoryKey> data) {
        super(R.layout.item_history_key, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryKey item) {
        helper.setText(R.id.titleText, item.key)
                .addOnClickListener(R.id.closeImage);
    }
}
