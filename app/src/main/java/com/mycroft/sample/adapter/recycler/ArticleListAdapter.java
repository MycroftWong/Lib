package com.mycroft.sample.adapter.recycler;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.core.text.HtmlCompat;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mycroft.lib.net.GlideApp;
import com.mycroft.sample.R;
import com.mycroft.sample.model.Article;
import com.mycroft.sample.model.ArticleTypeModel;

import java.util.List;

/**
 * @author mycroft
 */
public class ArticleListAdapter extends BaseMultiItemQuickAdapter<ArticleTypeModel, BaseViewHolder> {

    public ArticleListAdapter(List<ArticleTypeModel> data) {
        super(data);
        addItemType(ArticleTypeModel.TYPE_TEXT, R.layout.item_article_list_text);
        addItemType(ArticleTypeModel.TYPE_IMAGE, R.layout.item_article_list_image);

    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleTypeModel articleTypeModel) {
        Article item = articleTypeModel.getArticle();

        if (ArticleTypeModel.TYPE_IMAGE == articleTypeModel.getItemType()) {
            ImageView imageView = helper.getView(R.id.imageView);
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

    private final StringBuilder builder = new StringBuilder();

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
