package com.mycroft.sample.adapter.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mycroft.sample.fragment.ArticleListFragment;
import com.mycroft.sample.model.Category;

/**
 * @author wangqiang
 */
public class CategoryDetailPagerAdapter extends FragmentStatePagerAdapter {

    private final Category category;

    public CategoryDetailPagerAdapter(@NonNull FragmentManager fm, Category category) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.category = category;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ArticleListFragment.newInstance("article/list/%d/json?cid=" + category.getChildren().get(position).getId(), 0);
    }

    @Override
    public int getCount() {
        return category.getChildren().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return category.getChildren().get(position).getName();
    }
}
