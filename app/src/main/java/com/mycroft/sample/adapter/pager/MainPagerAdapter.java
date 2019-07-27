package com.mycroft.sample.adapter.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mycroft.sample.fragment.ArticleListFragment;
import com.mycroft.sample.fragment.CategoryFragment;
import com.mycroft.sample.fragment.OfficialAccountFragment;
import com.mycroft.sample.fragment.ProjectFragment;
import com.mycroft.sample.fragment.ToolsFragment;

/**
 * 主页adapter
 *
 * @author wangqiang
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ArticleListFragment.newInstance("/article/list/%d/json", 1);
            case 1:
                return CategoryFragment.newInstance();
            case 2:
                return OfficialAccountFragment.newInstance();
            case 3:
                return ToolsFragment.newInstance();
            case 4:
                return ProjectFragment.newInstance();
            default:
                return ArticleListFragment.newInstance("/article/list/%d/json", 1);
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
