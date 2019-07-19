package com.mycroft.sample.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mycroft.sample.adapter.ToolsAdapter;

/**
 * @author mycroft
 */
public class ToolsHeader extends AbstractExpandableItem<ToolsContent> implements MultiItemEntity {

    @Override
    public int getLevel() {
        return 0;
    }

    private final Tools tools;

    public ToolsHeader(Tools tools) {
        this.tools = tools;
        for (Article item : tools.getArticles()) {
            addSubItem(new ToolsContent(item));
        }
    }

    @Override
    public int getItemType() {
        return ToolsAdapter.TYPE_HEADER;
    }

    public Tools getTools() {
        return tools;
    }
}

