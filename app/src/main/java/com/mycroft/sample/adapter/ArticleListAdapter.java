package com.mycroft.sample.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mycroft.sample.R;
import com.mycroft.sample.model.Article;

import java.util.List;

/**
 * @author mycroft
 */
public class ArticleListAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {

    public ArticleListAdapter(@Nullable List<Article> data) {
        super(R.layout.item_article, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article item) {
        helper.setText(R.id.nameText, item.getAuthor())
                .setText(R.id.dateText, item.getNiceDate())
                .setText(R.id.contentText, item.getTitle())
                .setText(R.id.chapterText, item.getSuperChapterName());
    }

}
