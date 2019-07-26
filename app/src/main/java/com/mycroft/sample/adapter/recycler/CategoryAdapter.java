package com.mycroft.sample.adapter.recycler;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.donkingliang.labels.LabelsView;
import com.mycroft.sample.R;
import com.mycroft.sample.model.Category;

import java.util.List;

/**
 * @author mycroft
 */
public class CategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    public static final int TYPE_HEADER = 1;
    public static final int TYPE_CONTENT = 2;

    public CategoryAdapter(List<Category> data) {
        super(R.layout.item_category, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Category item) {
        helper.setText(R.id.titleText, item.getName());

        LabelsView labelsView = helper.getView(R.id.labelsView);
        labelsView.setLabels(item.getChildren(), (label, position, data) -> data.getName());
    }
}