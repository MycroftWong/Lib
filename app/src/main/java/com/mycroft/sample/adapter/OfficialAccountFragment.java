package com.mycroft.sample.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.billy.android.loading.Gloading;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.mycroft.sample.R;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.fragment.OfficialAccountArticleFragment;
import com.mycroft.sample.model.OfficialAccount;
import com.mycroft.sample.net.NetService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class OfficialAccountFragment extends CommonFragment {

    public static OfficialAccountFragment newInstance() {

        Bundle args = new Bundle();

        OfficialAccountFragment fragment = new OfficialAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Gloading.Holder holder;

    private final List<OfficialAccount> officialAccountList = new ArrayList<>();

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_official_account, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        holder = Gloading.getDefault().wrap(view).withRetry(this::loadData);
        holder.showLoading();
        return holder.getWrapper();
    }

    @Override
    public void onResume() {
        super.onResume();
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
                    viewPager.setAdapter(new OfficialAccountAdapter());
                    tabLayout.setupWithViewPager(viewPager);

                }, throwable -> {
                    holder.showLoadFailed();
                    ToastUtils.showShort(throwable.getMessage());
                }, () -> disposable = null);
    }

    private class OfficialAccountAdapter extends FragmentPagerAdapter {

        public OfficialAccountAdapter() {
            super(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return OfficialAccountArticleFragment.newInstance(officialAccountList.get(position));
        }

        @Override
        public int getCount() {
            return officialAccountList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return officialAccountList.get(position).getName();
        }
    }

    ;
}
