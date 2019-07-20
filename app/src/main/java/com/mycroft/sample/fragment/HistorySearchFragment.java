package com.mycroft.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.mycroft.lib.util.BaseQuickAdapterUtil;
import com.mycroft.lib.util.DisposableUtil;
import com.mycroft.sample.R;
import com.mycroft.sample.adapter.HistorySearchAdapter;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.dao.HistoryKeyService;
import com.mycroft.sample.model.HistoryKey;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return view;
    }

    private Disposable disposable;

    @Override
    public void onResume() {
        super.onResume();
        disposable = HistoryKeyService.getInstance()
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
        HistoryKeyService.deleteHistoryKey(getContext());
    };
}
