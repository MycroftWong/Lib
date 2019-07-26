package com.mycroft.sample.adapter.recycler;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.core.text.HtmlCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mycroft.lib.net.GlideApp;
import com.mycroft.sample.R;
import com.mycroft.sample.model.Article;

import java.util.List;

/**
 * @author mycroft
 */
public class SearchResultAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {

    public SearchResultAdapter(List<Article> data) {
        super(R.layout.item_search_result, data);
    }

    private final StringBuilder builder = new StringBuilder();

    @Override
    protected void convert(BaseViewHolder helper, Article item) {
        ImageView imageView = helper.getView(R.id.imageView);
        if (TextUtils.isEmpty(item.getEnvelopePic())) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            GlideApp.with(imageView)
                    .load(item.getEnvelopePic())
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }

        helper.setText(R.id.titleText, HtmlCompat.fromHtml(item.getTitle(), HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE))
                .setText(R.id.chapterText, buildContent(item))
                .setText(R.id.authorText, item.getAuthor())
                .setText(R.id.dateText, item.getNiceDate());
    }

    private String buildContent(Article result) {
        builder.setLength(0);

        boolean hasSuperChapter = !TextUtils.isEmpty(result.getSuperChapterName());
        boolean hasChapter = !TextUtils.isEmpty(result.getChapterName());
        String content;
        if (hasSuperChapter && hasChapter) {
            content = builder.append(result.getSuperChapterName()).append("·").append(result.getChapterName()).toString();
        } else if (!hasSuperChapter && !hasChapter) {
            content = "";
        } else {
            content = result.getSuperChapterName() == null ? result.getChapterName() : result.getSuperChapterName();
        }

        return content;
    }
}
