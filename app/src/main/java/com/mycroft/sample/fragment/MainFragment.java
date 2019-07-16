package com.mycroft.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.billy.android.loading.Gloading;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mycroft.lib.util.BaseQuickAdapterUtil;
import com.mycroft.lib.util.DisposableUtil;
import com.mycroft.sample.R;
import com.mycroft.sample.activity.WebViewActivity;
import com.mycroft.sample.adapter.ArticleListAdapter;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.model.Article;
import com.mycroft.sample.net.NetService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author mycroft
 */
public class MainFragment extends CommonFragment {

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private final List<Article> articles = new ArrayList<>();

    private BaseQuickAdapter<Article, BaseViewHolder> adapter;

    private Gloading.Holder holder;
    private SmartRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        holder = Gloading.getDefault().wrap(refreshLayout).withRetry(() -> loadData(nextPage));

        adapter = new ArticleListAdapter(articles);
        adapter.setOnItemClickListener((a, v, position) -> startActivity(WebViewActivity.getIntent(getContext(), articles.get(position))));
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshLoadMoreListener(refreshLoadMoreListener);

        return holder.getWrapper();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (articles.isEmpty()) {
            loadData(0);
        }
    }

    private int nextPage = 0;

    private Disposable disposable;

    private void loadData(int page) {
        holder.showLoading();
        if (disposable == null) {
            disposable = NetService.getInstance().getArticleList(page)
                    .subscribe(articleListModel -> {
                                holder.showLoadSuccess();
                                nextPage = articleListModel.getCurPage();
                                articles.addAll(articleListModel.getDatas());
                                adapter.notifyDataSetChanged();
                                finishRefresh();
                            },
                            throwable -> {
                                LogUtils.e(throwable);
                                ToastUtils.showShort(throwable.getMessage());
                                if (articles.isEmpty()) {
                                    holder.showLoadFailed();
                                }
                                finishRefresh();
                            });
        }
    }

    private void finishRefresh() {
        disposable = null;
        if (RefreshState.Refreshing == refreshLayout.getState()) {
            refreshLayout.finishRefresh();
        }
        if (RefreshState.Loading == refreshLayout.getState()) {
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onDestroyView() {
        BaseQuickAdapterUtil.releaseAdapter(adapter);
        adapter = null;
        DisposableUtil.dispose(disposable);
        disposable = null;

        super.onDestroyView();
    }

    private final OnRefreshLoadMoreListener refreshLoadMoreListener = new OnRefreshLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            loadData(nextPage);
        }

        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            loadData(0);
        }
    };
}
