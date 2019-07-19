package com.mycroft.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.mycroft.lib.util.BaseQuickAdapterUtil;
import com.mycroft.lib.util.DisposableUtil;
import com.mycroft.lib.view.Loading;
import com.mycroft.lib.view.LoadingHolder;
import com.mycroft.sample.R;
import com.mycroft.sample.adapter.ToolsAdapter;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.model.Tools;
import com.mycroft.sample.model.ToolsHeader;
import com.mycroft.sample.net.NetService;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * @author mycroft
 */
public class ToolsFragment extends CommonFragment {

    public static ToolsFragment newInstance() {

        Bundle args = new Bundle();

        ToolsFragment fragment = new ToolsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private LoadingHolder holder;
    private ToolsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new ToolsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        holder = Loading.getDefault().wrap(view).withRetry(this::loadData);
        holder.showLoading();
        return holder.getWrapper();
    }

    private Disposable disposable;

    private void loadData() {
        if (disposable != null) {
            return;
        }

        disposable = NetService.getInstance().getToolList()
                .subscribe(toolsList -> {
                    holder.showLoadSuccess();
                    for (Tools item : toolsList) {
                        adapter.addData(new ToolsHeader(item));
                    }
                }, throwable -> {
                    holder.showLoadFailed();
                    ToastUtils.showShort(throwable.getMessage());
                }, () -> disposable = null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter.getData().isEmpty()) {
            loadData();
        } else {
            holder.showLoadSuccess();
        }
    }

    @Override
    public void onDestroyView() {
        DisposableUtil.dispose(disposable);
        disposable = null;

        BaseQuickAdapterUtil.releaseAdapter(adapter);
        adapter = null;
        super.onDestroyView();
    }
}
