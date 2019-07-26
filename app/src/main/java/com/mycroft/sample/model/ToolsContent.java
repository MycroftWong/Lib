package com.mycroft.sample.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mycroft.sample.adapter.recycler.ToolsAdapter;

/**
 * @author mycroft
 */
public final class ToolsContent implements MultiItemEntity {

    private final Article article;

    public ToolsContent(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }

    @Override
    public int getItemType() {
        return ToolsAdapter.TYPE_CONTENT;
    }
}
