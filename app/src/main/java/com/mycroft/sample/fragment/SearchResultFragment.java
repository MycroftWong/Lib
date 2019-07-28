package com.mycroft.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.mycroft.lib.util.BaseQuickAdapterUtil;
import com.mycroft.lib.util.DisposableUtil;
import com.mycroft.sample.R;
import com.mycroft.sample.activity.WebViewActivity;
import com.mycroft.sample.adapter.recycler.SearchResultAdapter;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.model.Article;
import com.mycroft.sample.net.NetService;
import com.mycroft.sample.service.HistoryKeyServiceImpl;
import com.mycroft.sample.service.IHistoryKeyService;
import com.mycroft.sample.shared.SearchViewModel;
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
public class SearchResultFragment extends CommonFragment {

    private static final int START_PAGE = 1;

    public static SearchResultFragment newInstance() {
        Bundle args = new Bundle();
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private IHistoryKeyService historyKeyService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        historyKeyService = HistoryKeyServiceImpl.getInstance();

        ViewModelProviders.of(getActivity()).get(SearchViewModel.class)
                .getSearchKey()
                .observe(this, s -> {
                    key = s;
                    searchResultList.clear();
                    adapter.notifyDataSetChanged();
                    loadData(START_PAGE);
                });
    }

    private SmartRefreshLayout refreshLayout;

    private RecyclerView recyclerView;

    private final List<Article> searchResultList = new ArrayList<>();

    private SearchResultAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verticle_refresh_recycler, container, false);

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshLoadMoreListener(refreshLoadMoreListener);

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new SearchResultAdapter(searchResultList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((a, v, position) ->
                startActivity(WebViewActivity.getIntent(getContext(), searchResultList.get(position))));

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onDestroyView() {

        DisposableUtil.dispose(disposable);
        disposable = null;

        BaseQuickAdapterUtil.releaseAdapter(adapter);
        adapter = null;

        super.onDestroyView();
    }

    private Disposable disposable;

    private String key;

    private int nextPage = START_PAGE;

    private void loadData(int page) {
        if (disposable != null) {
            return;
        }

        if (page == START_PAGE) {
            showLoadingDialog();
            searchResultList.clear();
        }

        historyKeyService.addHistoryKey(key);

        disposable = NetService.getInstance().search(key, page)
                .subscribe(listData -> {
                            nextPage = listData.getCurPage() + 1;
                            searchResultList.addAll(listData.getDatas());

                            adapter.notifyDataSetChanged();
                            if (listData.getCurPage() == START_PAGE) {
                                recyclerView.scrollToPosition(0);
                            }

                        }, throwable -> {
                            ToastUtils.showShort(throwable.getMessage());
                            finishRefresh();
                        },
                        this::finishRefresh);
    }

    private void finishRefresh() {
        disposable = null;
        hideLoadingDialog();
        if (RefreshState.Refreshing == refreshLayout.getState()) {
            refreshLayout.finishRefresh();
        }
        if (RefreshState.Loading == refreshLayout.getState()) {
            refreshLayout.finishLoadMore();
        }
    }

    private final OnRefreshLoadMoreListener refreshLoadMoreListener = new OnRefreshLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            loadData(nextPage);
        }

        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            loadData(START_PAGE);
        }
    };
}
