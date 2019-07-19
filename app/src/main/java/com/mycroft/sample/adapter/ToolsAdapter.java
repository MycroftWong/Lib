package com.mycroft.sample.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.donkingliang.labels.LabelsView;
import com.mycroft.sample.R;
import com.mycroft.sample.model.Tools;

import java.util.List;

/**
 * @author mycroft
 */
public class ToolsAdapter extends BaseQuickAdapter<Tools, BaseViewHolder> {

    public ToolsAdapter(@Nullable List<Tools> data) {
        super(R.layout.item_category, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Tools item) {
        helper.setText(R.id.titleText, item.getName());
        LabelsView labelsView = helper.getView(R.id.labelsView);

        labelsView.setLabels(item.getArticles(), (label, position, data) -> data.getTitle());
    }
}
