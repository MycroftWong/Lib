package com.mycroft.sample.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mycroft.sample.fragment.ArticleListFragment;
import com.mycroft.sample.model.OfficialAccount;

import java.util.List;

/**
 * @author mycroft
 */
public class OfficialAccountAdapter extends FragmentPagerAdapter {

    private final List<OfficialAccount> officialAccountList;

    public OfficialAccountAdapter(@NonNull FragmentManager fragmentManager, List<OfficialAccount> officialAccountList) {
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.officialAccountList = officialAccountList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ArticleListFragment.newInstance("wxarticle/list/" + officialAccountList.get(position).getId() + "/%d/json", 1);
    }

    @Override
    public int getCount() {
        return officialAccountList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return officialAccountList.get(position).getName();
    }
}
