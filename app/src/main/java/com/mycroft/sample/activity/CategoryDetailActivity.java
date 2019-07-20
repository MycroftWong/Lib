package com.mycroft.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.mycroft.sample.R;
import com.mycroft.sample.adapter.CategoryDetailAdapter;
import com.mycroft.sample.common.CommonActivity;
import com.mycroft.sample.model.Category;
import com.mycroft.sample.view.OnTabSelectedAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangqiang
 */
public class CategoryDetailActivity extends CommonActivity {

    private static final String EXTRA_CATEGORY = "category.extra";

    public static Intent getIntent(@NonNull Context context, @NonNull Category category) {
        Intent intent = new Intent(context, CategoryDetailActivity.class);
        intent.putExtra(EXTRA_CATEGORY, category);
        return intent;
    }

    @Override
    protected int getResId() {
        return R.layout.activity_category_detail;
    }

    private Category category;

    @Override
    protected void initFields(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            category = getIntent().getParcelableExtra(EXTRA_CATEGORY);
        } else {
            category = savedInstanceState.getParcelable(EXTRA_CATEGORY);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_CATEGORY, category);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void initViews() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimaryDark)
                .statusBarDarkFont(true)
                .init();

        ButterKnife.bind(this);

        titleBar.setTitle(category.getName());
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });

        viewPager.setAdapter(new CategoryDetailAdapter(getSupportFragmentManager(), category));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new OnTabSelectedAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }
        });
    }

    @Override
    protected void loadData() {

    }

}
