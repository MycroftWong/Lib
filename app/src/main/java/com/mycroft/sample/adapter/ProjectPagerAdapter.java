package com.mycroft.sample.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mycroft.sample.fragment.ArticleListFragment;
import com.mycroft.sample.model.Project;

import java.util.List;

/**
 * 项目分类
 *
 * @author mycroft
 */
public class ProjectPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Project> projectList;

    public ProjectPagerAdapter(@NonNull FragmentManager fm, List<Project> projectList) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.projectList = projectList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ArticleListFragment.newInstance("project/list/%d/json?cid=" + projectList.get(position).getId(), 1);
    }

    @Override
    public int getCount() {
        return projectList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return projectList.get(position).getName();
    }
}
