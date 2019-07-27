package com.mycroft.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
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
    @BindView(R.id.container)
    ViewPager container;
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
    protected void initViews() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimaryDark)
                .statusBarDarkFont(true)
                .init();

        ButterKnife.bind(this);

        container.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

        navigationView.setOnNavigationItemSelectedListener(item -> {
            titleBar.setTitle(item.getTitle());
            switch (item.getItemId()) {
                case R.id.mainMenu:
                    container.setCurrentItem(0, false);
                    break;
                case R.id.categoryMenu:
                    container.setCurrentItem(1, false);
                    break;
                case R.id.officialAccountMenu:
                    container.setCurrentItem(2, false);
                    break;
                case R.id.toolMenu:
                    container.setCurrentItem(3, false);
                    break;
                case R.id.projectMenu:
                    container.setCurrentItem(4, false);
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
        super.onDestroy();
    }
}
