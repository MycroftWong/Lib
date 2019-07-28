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
import com.mycroft.sample.adapter.recycler.OfficialAccountAdapter;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.model.OfficialAccount;
import com.mycroft.sample.net.NetService;
import com.mycroft.sample.view.OnTabSelectedAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 公众号
 *
 * @author mycroft
 */
public class OfficialAccountFragment extends CommonFragment {

    public static OfficialAccountFragment newInstance() {
        Bundle args = new Bundle();
        OfficialAccountFragment fragment = new OfficialAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LoadingHolder holder;

    private final List<OfficialAccount> officialAccountList = new ArrayList<>();

    private OfficialAccountAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_official_account, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);

        adapter = new OfficialAccountAdapter(getChildFragmentManager(), officialAccountList);
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
        return holder.getWrapper();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (officialAccountList.isEmpty()) {
            holder.showLoading();
        } else {
            holder.showLoadSuccess();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (officialAccountList.isEmpty()) {
            loadData();
        }
    }

    private Disposable disposable;

    private void loadData() {
        if (disposable != null) {
            return;
        }

        disposable = NetService.getInstance().getOfficialAccountList()
                .subscribe(officialAccounts -> {
                    holder.showLoadSuccess();
                    officialAccountList.addAll(officialAccounts);
                    adapter.notifyDataSetChanged();
                }, throwable -> {
                    disposable = null;
                    holder.showLoadFailed();
                    ToastUtils.showShort(throwable.getMessage());
                }, () -> disposable = null);
    }
}
