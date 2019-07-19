package com.mycroft.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mycroft.lib.util.FragmentSwitcher;
import com.mycroft.sample.R;
import com.mycroft.sample.common.CommonActivity;
import com.mycroft.sample.fragment.ArticleListFragment;
import com.mycroft.sample.fragment.CategoryFragment;
import com.mycroft.sample.fragment.OfficialAccountFragment;
import com.mycroft.sample.fragment.ProjectFragment;
import com.mycroft.sample.fragment.ToolsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangqiang
 */
public class MainActivity extends CommonActivity {

    public static Intent getIntent(@NonNull Context context) {
        return new Intent(context, MainActivity.class);
    }

    @BindView(R.id.toolbar)
    MaterialToolbar toolbar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.navigationView)
    BottomNavigationView navigationView;

    @Override
    protected int getResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initFields(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);

        FragmentSwitcher fragmentSwitcher = new FragmentSwitcher(getSupportFragmentManager(), R.id.container, adapter);

        fragmentSwitcher.startFragment(0);

        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mainMenu:
                    fragmentSwitcher.startFragment(0);
                    break;
                case R.id.categoryMenu:
                    fragmentSwitcher.startFragment(1);
                    break;
                case R.id.officialAccountMenu:
                    fragmentSwitcher.startFragment(2);
                    break;
                case R.id.toolMenu:
                    fragmentSwitcher.startFragment(3);
                    break;
                case R.id.projectMenu:
                    fragmentSwitcher.startFragment(4);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    @Override
    protected void loadData(@Nullable Bundle savedInstanceState) {

    }

    private final FragmentSwitcher.FragmentAdapter adapter = position -> {
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
    };
}
