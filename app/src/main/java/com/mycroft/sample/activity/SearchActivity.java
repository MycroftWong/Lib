package com.mycroft.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.KeyboardUtils;
import com.mycroft.sample.R;
import com.mycroft.sample.common.CommonActivity;
import com.mycroft.sample.fragment.HistorySearchFragment;
import com.mycroft.sample.fragment.SearchResultFragment;
import com.mycroft.sample.shared.SearchViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author mycroft
 */
public class SearchActivity extends CommonActivity {

    public static Intent getIntent(@NonNull Context context) {
        return new Intent(context, SearchActivity.class);
    }

    private static final String TAG_HISTORY = "history.tag";
    private static final String TAG_RESULT = "result.tag";

    @BindView(R.id.backImage)
    ImageView backImage;
    @BindView(R.id.searchEdit)
    EditText searchEdit;

    @Override
    protected int getResId() {
        return R.layout.activity_search;
    }

    private SearchViewModel searchViewModel;

    @Override
    protected void initFields(@Nullable Bundle savedInstanceState) {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        searchViewModel.getSearchKey().observe(this, s -> {
            search();
            searchEdit.setText(s);
            searchEdit.setSelection(s.length());
        });
    }

    private HistorySearchFragment historySearchFragment;

    private SearchResultFragment searchResultFragment;

    @Override
    protected void initViews() {
        ButterKnife.bind(this);

        searchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                CharSequence sequence = searchEdit.getText();
                if (!TextUtils.isEmpty(sequence)) {
                    searchViewModel.setSearchKey(sequence.toString());
                }
                return true;
            }

            return false;
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_HISTORY);
        if (fragment == null) {
            historySearchFragment = HistorySearchFragment.newInstance();
        } else {
            historySearchFragment = (HistorySearchFragment) fragment;
        }

        if (!historySearchFragment.isAdded()) {
            fragmentTransaction.add(R.id.container, historySearchFragment, TAG_HISTORY);
        }
        fragmentTransaction.show(historySearchFragment);

        fragment = fragmentManager.findFragmentByTag(TAG_RESULT);
        if (fragment == null) {
            searchResultFragment = SearchResultFragment.newInstance();
        } else {
            searchResultFragment = (SearchResultFragment) fragment;
        }

        if (!searchResultFragment.isAdded()) {
            fragmentTransaction.add(R.id.container, searchResultFragment, TAG_RESULT);
        }
        fragmentTransaction.hide(searchResultFragment);

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.backImage)
    public void onViewClicked() {
        if (searchResultFragment != null && searchResultFragment.isVisible()) {
            showHistory();
        } else {
            finish();
        }
    }

    private void search() {
        KeyboardUtils.hideSoftInput(searchEdit);
        showResult();
    }

    private void showResult() {
        getSupportFragmentManager().beginTransaction()
                .show(searchResultFragment)
                .hide(historySearchFragment)
//                .setMaxLifecycle(searchResultFragment, Lifecycle.State.RESUMED)
//                .setMaxLifecycle(historySearchFragment, Lifecycle.State.CREATED)
                .commit();
    }

    private void showHistory() {
        getSupportFragmentManager().beginTransaction()
                .show(historySearchFragment)
                .hide(searchResultFragment)
//                .setMaxLifecycle(historySearchFragment, Lifecycle.State.RESUMED)
//                .setMaxLifecycle(searchResultFragment, Lifecycle.State.CREATED)
                .commit();
    }
}
