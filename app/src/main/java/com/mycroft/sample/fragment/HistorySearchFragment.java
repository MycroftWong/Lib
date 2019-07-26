package com.mycroft.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.mycroft.lib.util.BaseQuickAdapterUtil;
import com.mycroft.lib.util.DisposableUtil;
import com.mycroft.sample.R;
import com.mycroft.sample.adapter.recycler.HistorySearchAdapter;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.model.HistoryKey;
import com.mycroft.sample.service.HistoryKeyServiceImpl;
import com.mycroft.sample.service.IHistoryKeyService;
import com.mycroft.sample.shared.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 历史搜索
 * 1. 加载热门搜索关键词
 * 2. 操作搜索历史记录
 *
 * @author wangqiang
 */
public class HistorySearchFragment extends CommonFragment {

    public static HistorySearchFragment newInstance() {

        Bundle args = new Bundle();

        HistorySearchFragment fragment = new HistorySearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SearchViewModel searchViewModel;

    private IHistoryKeyService historyKeyService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyKeyService = HistoryKeyServiceImpl.getInstance();
        searchViewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
    }

    private final List<HistoryKey> historySearchKey = new ArrayList<>();

    private HistorySearchAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_search, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new HistorySearchAdapter(historySearchKey);
        adapter.addHeaderView(createHeaderView(inflater, recyclerView));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setOnItemClickListener((a, v, position) -> searchViewModel.setSearchKey(historySearchKey.get(position).key));

        adapter.setOnItemChildClickListener((a, v, position) -> {
            HistoryKey historyKey = historySearchKey.remove(position);
            historyKeyService.deleteHistoryKey(historyKey);
            adapter.notifyDataSetChanged();
        });

        return view;
    }

    private Disposable disposable;

    @Override
    public void onResume() {
        super.onResume();
        disposable = historyKeyService
                .getAllHistoryKey()
                .subscribe(historyKeys -> {
                            historySearchKey.clear();
                            historySearchKey.addAll(historyKeys);
                            adapter.notifyDataSetChanged();
                        }, LogUtils::e,
                        () -> disposable = null);
    }

    @Override
    public void onDestroyView() {
        DisposableUtil.dispose(disposable);
        disposable = null;
        BaseQuickAdapterUtil.releaseAdapter(adapter);
        adapter = null;
        super.onDestroyView();
    }

    private View createHeaderView(@NonNull LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.label_search_history, container, false);
        TextView clearAllText = view.findViewById(R.id.clearAllText);
        clearAllText.setOnClickListener(clearClickListener);
        return view;
    }

    private final View.OnClickListener clearClickListener = view -> {
        historySearchKey.clear();
        adapter.notifyDataSetChanged();
        historyKeyService.clearHistoryKey();
    };
}
