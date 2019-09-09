package com.mycroft.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.mycroft.lib.util.BaseQuickAdapterUtil;
import com.mycroft.lib.util.DisposableUtil;
import com.mycroft.lib.view.Loading;
import com.mycroft.lib.view.LoadingHolder;
import com.mycroft.sample.R;
import com.mycroft.sample.activity.WebViewActivity;
import com.mycroft.sample.adapter.recycler.ArticleListAdapter;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.model.Article;
import com.mycroft.sample.model.ArticleTypeModel;
import com.mycroft.sample.net.NetService;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 通用的文章列表页面
 *
 * @author mycroft
 */
public final class ArticleListFragment extends CommonFragment {

    private static final String ARGS_ARTICLE = "article.args";
    private static final String ARGS_START_PAGE = "start_page.args";

    private static final String SAVED_ARTICLES = "articleList.saved";

    public static ArticleListFragment newInstance(String url, int startPage) {

        Bundle args = new Bundle();
        args.putString(ARGS_ARTICLE, url);
        args.putInt(ARGS_START_PAGE, startPage);
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String articleUrl;
    private int startPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == savedInstanceState) {
            articleUrl = getArguments().getString(ARGS_ARTICLE);
            startPage = getArguments().getInt(ARGS_START_PAGE);
        } else {
            articleUrl = savedInstanceState.getString(ARGS_ARTICLE);
            startPage = savedInstanceState.getInt(ARGS_START_PAGE);
            List<ArticleTypeModel> articles = savedInstanceState.getParcelableArrayList(SAVED_ARTICLES);
            if (null != articles && !articles.isEmpty()) {
                articleTypeModels.addAll(articles);
            }
        }

        nextPage = startPage;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARGS_ARTICLE, articleUrl);
        outState.putInt(ARGS_START_PAGE, startPage);
        outState.putParcelableArrayList(SAVED_ARTICLES, articleTypeModels);
    }

    private final ArrayList<ArticleTypeModel> articleTypeModels = new ArrayList<>();

    private ArticleListAdapter adapter;

    private LoadingHolder holder;
    private SmartRefreshLayout refreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vertical_refresh_recycler, container, false);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        adapter = new ArticleListAdapter(articleTypeModels);
        adapter.setOnItemClickListener((a, v, position) -> startActivity(WebViewActivity.getIntent(getContext(), articleTypeModels.get(position).getArticle())));
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshLoadMoreListener(refreshLoadMoreListener);

        holder = Loading.getDefault().wrap(refreshLayout).withRetry(() -> {
            holder.showLoading();
            loadData(startPage);
        });

        return holder.getWrapper();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (articleTypeModels.isEmpty()) {
            holder.showLoading();
        } else {
            holder.showLoadSuccess();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (articleTypeModels.isEmpty()) {
            loadData(startPage);
        }
    }

    private int nextPage;

    private Disposable disposable;

    private void loadData(int page) {
        if (disposable == null) {
            holder.showLoading();
            disposable = NetService.getInstance().getArticleList(articleUrl, page)
                    .subscribe(articleListModel -> {
                                holder.showLoadSuccess();

                                if (page == startPage) {
                                    articleTypeModels.clear();
                                }
                                nextPage = page + 1;
                                for (Article item : articleListModel.getDatas()) {
                                    articleTypeModels.add(new ArticleTypeModel(item));
                                }
                                adapter.notifyDataSetChanged();
                            },
                            throwable -> {
                                LogUtils.e(throwable);
                                ToastUtils.showShort(throwable.getMessage());
                                if (articleTypeModels.isEmpty()) {
                                    holder.showLoadFailed();
                                }
                                finishRefresh();
                            },
                            this::finishRefresh);
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
            loadData(startPage);
        }
    };
}
