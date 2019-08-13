package com.mycroft.sample.adapter.pager;

import androidx.fragment.app.Fragment;

import com.mycroft.lib.util.FragmentSwitcher;
import com.mycroft.sample.fragment.CategoryFragment;
import com.mycroft.sample.fragment.HomeFragment;
import com.mycroft.sample.fragment.OfficialAccountFragment;
import com.mycroft.sample.fragment.ProjectFragment;
import com.mycroft.sample.fragment.ToolsFragment;

/**
 * 主页pager adapter
 *
 * @author wangqiang
 */
public class MainPagerAdapter implements FragmentSwitcher.FragmentAdapter {
    @Override
    public Fragment getFragmentBy(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return CategoryFragment.newInstance();
            case 2:
                return OfficialAccountFragment.newInstance();
            case 3:
                return ToolsFragment.newInstance();
            case 4:
                return ProjectFragment.newInstance();
            default:
                return HomeFragment.newInstance();
        }
    }
}
