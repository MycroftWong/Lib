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
import com.mycroft.sample.activity.CategoryDetailActivity;
import com.mycroft.sample.adapter.recycler.CategoryAdapter;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.model.Category;
import com.mycroft.sample.net.NetService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author mycroft
 */
public class CategoryFragment extends CommonFragment {

    private static final String STATE_CATEGORY_LIST = "category_list.state";

    public static CategoryFragment newInstance() {

        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private final ArrayList<Category> categoryList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            List<Category> categories = savedInstanceState.getParcelableArrayList(STATE_CATEGORY_LIST);
            if (categories != null) {
                categoryList.addAll(categories);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_CATEGORY_LIST, categoryList);
    }

    private LoadingHolder holder;
    private CategoryAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new CategoryAdapter(categoryList);

        adapter.setOnItemClickListener((adapter1, view1, position) ->
                startActivity(CategoryDetailActivity.getIntent(getContext(), categoryList.get(position))));

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        holder = Loading.getDefault().wrap(view).withRetry(() -> {
            holder.showLoading();
            loadData();
        });

        return holder.getWrapper();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (categoryList.isEmpty()) {
            holder.showLoading();
        } else {
            holder.showLoadSuccess();
        }
    }

    private Disposable disposable;

    private void loadData() {
        if (disposable != null) {
            return;
        }

        disposable = NetService.getInstance().getCategoryList()
                .subscribe(categories -> {
                    holder.showLoadSuccess();
                    categoryList.addAll(categories);
                    adapter.notifyDataSetChanged();
                }, throwable -> {
                    disposable = null;
                    holder.showLoadFailed();
                    ToastUtils.showShort(throwable.getMessage());
                }, () -> disposable = null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter.getData().isEmpty()) {
            loadData();
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
