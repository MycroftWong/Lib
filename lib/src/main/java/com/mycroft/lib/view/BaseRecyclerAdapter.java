package com.mycroft.lib.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mycroft.lib.R;

import java.util.List;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Mycroft on 2017/1/5.
 */

public abstract class BaseRecyclerAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseRecyclerAdapter(RecyclerView parent, int layoutResId, List<T> data, @StringRes int emptyTextRes) {
        super(layoutResId, data);
        View emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
        TextView emptyTextView = emptyView.findViewById(R.id.empty_text_view);
        emptyTextView.setText(emptyTextRes);
        super.setEmptyView(emptyView);
    }

    public BaseRecyclerAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }
}
