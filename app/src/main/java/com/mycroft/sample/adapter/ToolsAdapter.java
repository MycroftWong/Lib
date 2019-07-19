package com.mycroft.sample.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mycroft.sample.activity.WebViewActivity;
import com.mycroft.sample.model.ToolsContent;
import com.mycroft.sample.model.ToolsHeader;

import java.util.List;

/**
 * @author mycroft
 */
public class ToolsAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_HEADER = 1;

    public static final int TYPE_CONTENT = 2;

    public ToolsAdapter(@Nullable List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_HEADER, android.R.layout.simple_list_item_1);
        addItemType(TYPE_CONTENT, android.R.layout.simple_expandable_list_item_1);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_HEADER:
                final ToolsHeader header = (ToolsHeader) item;
                helper.setText(android.R.id.text1, header.getTools().getName());
                helper.itemView.setOnClickListener(v -> {
                    int pos = helper.getAdapterPosition();
                    if (header.isExpanded()) {
                        collapse(pos);
                    } else {
                        expand(pos);
                    }
                });
                break;
            case TYPE_CONTENT:
                final ToolsContent content = (ToolsContent) item;
                helper.setText(android.R.id.text1, content.getArticle().getTitle());
                helper.itemView.setOnClickListener(v ->
                        helper.itemView.getContext()
                                .startActivity(WebViewActivity.getIntent(helper.itemView.getContext(), content.getArticle()))
                );
                break;
            default:
                break;
        }
    }
}