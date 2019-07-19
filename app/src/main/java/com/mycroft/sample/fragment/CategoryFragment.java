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
import com.blankj.utilcode.util.ToastUtils;
import com.mycroft.lib.util.BaseQuickAdapterUtil;
import com.mycroft.lib.util.DisposableUtil;
import com.mycroft.sample.R;
import com.mycroft.sample.adapter.CategoryAdapter;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.net.NetService;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * @author mycroft
 */
public class CategoryFragment extends CommonFragment {

    public static CategoryFragment newInstance() {

        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Gloading.Holder holder;
    private CategoryAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new CategoryAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        holder = Gloading.getDefault().wrap(view).withRetry(this::loadData);
        holder.showLoading();

        adapter.expandAll();

        return holder.getWrapper();
    }

    private Disposable disposable;

    private void loadData() {
        if (disposable != null) {
            return;
        }

        disposable = NetService.getInstance().getCategoryList()
                .subscribe(categories -> {
                    holder.showLoadSuccess();
                    adapter.addData(categories);
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
        BaseQuickAdapterUtil.releaseAdapter(adapter);
        adapter = null;

        DisposableUtil.dispose(disposable);
        disposable = null;
        super.onDestroyView();
    }
}
