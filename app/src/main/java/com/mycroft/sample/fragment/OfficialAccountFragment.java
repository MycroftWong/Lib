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
import com.mycroft.sample.adapter.OfficialAccountAdapter;
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

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_official_account, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        holder = Loading.getDefault().wrap(view).withRetry(this::loadData);
        holder.showLoading();
        return holder.getWrapper();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (officialAccountList.isEmpty()) {
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

        disposable = NetService.getInstance().getOfficialAccountList()
                .subscribe(officialAccounts -> {
                    holder.showLoadSuccess();
                    officialAccountList.addAll(officialAccounts);
                    initRealView();
                }, throwable -> {
                    holder.showLoadFailed();
                    ToastUtils.showShort(throwable.getMessage());
                }, () -> disposable = null);
    }

    private void initRealView() {
        viewPager.setAdapter(new OfficialAccountAdapter(getChildFragmentManager(), officialAccountList));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new OnTabSelectedAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }
        });
    }
}
