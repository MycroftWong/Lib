package com.mycroft.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.mycroft.lib.view.Loading;
import com.mycroft.lib.view.LoadingHolder;
import com.mycroft.sample.R;
import com.mycroft.sample.adapter.pager.ProjectPagerAdapter;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.model.Project;
import com.mycroft.sample.net.NetService;
import com.mycroft.sample.view.OnTabSelectedAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ProjectFragment extends CommonFragment {

    public static ProjectFragment newInstance() {

        Bundle args = new Bundle();

        ProjectFragment fragment = new ProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LoadingHolder holder;

    private final List<Project> projectList = new ArrayList<>();

    private ProjectPagerAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_official_account, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        adapter = new ProjectPagerAdapter(getChildFragmentManager(), projectList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new OnTabSelectedAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }
        });

        holder = Loading.getDefault().wrap(view).withRetry(() -> {
            holder.showLoading();
            loadData();
        });
        holder.showLoading();
        return holder.getWrapper();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (projectList.isEmpty()) {
            loadData();
        } else {
            holder.showLoadSuccess();
        }
    }

    private Disposable disposable;

    private void loadData() {
        if (disposable != null) {
            return;
        }

        disposable = NetService.getInstance().getProjectList()
                .subscribe(projects -> {

                    holder.showLoadSuccess();
                    projectList.addAll(projects);
                    adapter.notifyDataSetChanged();
                }, throwable -> {
                    disposable = null;
                    holder.showLoadFailed();
                    ToastUtils.showShort(throwable.getMessage());
                }, () -> disposable = null);
    }

}
