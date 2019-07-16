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
import com.mycroft.sample.fragment.MainFragment;

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

    }

    @Override
    protected void loadData(@Nullable Bundle savedInstanceState) {

    }

    private final FragmentSwitcher.FragmentAdapter adapter = position -> {
        switch (position) {
            case 0:
            default:
                return MainFragment.newInstance();
        }
    };
}
