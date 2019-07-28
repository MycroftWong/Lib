package com.mycroft.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.mycroft.lib.util.FragmentSwitcher;
import com.mycroft.sample.R;
import com.mycroft.sample.adapter.pager.MainPagerAdapter;
import com.mycroft.sample.common.CommonActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangqiang
 */
public class MainActivity extends CommonActivity {

    public static Intent getIntent(@NonNull Context context) {
        return new Intent(context, MainActivity.class);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.navigationView)
    BottomNavigationView navigationView;

    @Override
    protected int getResId() {
        return R.layout.activity_main;
    }

    private FragmentSwitcher fragmentSwitcher;

    @Override
    protected void initFields(@Nullable Bundle savedInstanceState) {
        fragmentSwitcher = new FragmentSwitcher(getSupportFragmentManager(), R.id.container, new MainPagerAdapter());
    }

    @Override
    protected void initViews() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimaryDark)
                .statusBarDarkFont(true)
                .init();

        ButterKnife.bind(this);

        fragmentSwitcher.startFragment(0);

        navigationView.setOnNavigationItemSelectedListener(item -> {
            titleBar.setTitle(item.getTitle());
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

        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
            }

            @Override
            public void onTitleClick(View v) {
            }

            @Override
            public void onRightClick(View v) {
                startActivity(SearchActivity.getIntent(MainActivity.this));
            }
        });

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onDestroy() {
        fragmentSwitcher.destroy();
        fragmentSwitcher = null;
        super.onDestroy();
    }
}
