package com.mycroft.lib.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;

import com.mycroft.lib.R;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * 只适合用于{@link ViewPager}懒加载，
 * 并且所有的{@link Fragment} 都是一次性加载出来的界面
 * <p>
 * 通用的带刷新界面的懒加载{@link Fragment}
 * <p>
 * Created by Mycroft on 2017/1/9.
 *
 * @author mycroft
 */
public abstract class BaseLazyFragment extends BaseFragment {

    /**
     * 检测声明周期中，是否已经构建视图
     */
    private boolean mViewCreated = false;

    protected ViewGroup mRealContentContainer;

    private ViewStub mViewStub;

    private ViewGroup mRefreshContainer;
    private View mRefreshingLabel;
    private ViewGroup mHintContainer;
    private Button mRefreshButton;

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common_lazy, container, false);

        mRealContentContainer = rootView.findViewById(R.id.real_content_container);

        mViewStub = rootView.findViewById(R.id.view_stub);
        mViewStub.setLayoutResource(getDataLayoutId());

        mRefreshContainer = rootView.findViewById(R.id.refresh_container);
        mRefreshingLabel = rootView.findViewById(R.id.refreshing_label);
        mHintContainer = rootView.findViewById(R.id.hint_container);
        mRefreshButton = rootView.findViewById(R.id.refresh_button);

        mRefreshButton.setOnClickListener(mOnRefreshClickListener);

        mViewCreated = true;
        if (mUserVisible) {
            realLoad();
        }
        return rootView;
    }

    /**
     * 获取layout id
     *
     * @return 真实数据layout
     */
    @LayoutRes
    protected abstract int getDataLayoutId();

    /**
     * 当刷新按钮被点击时的操作
     */
    protected abstract void refreshData();

    /**
     * 当开始第一次加载数据时调用
     */
    protected final void showRefreshWhenStart() {
        if (mRefreshContainer.isShown()) {
            mRefreshingLabel.setVisibility(View.VISIBLE);
            mHintContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 第一次加载数据时失败时调用
     */
    protected final void showRefreshWhenFailed() {
        if (mRefreshContainer.isShown()) {
            mRefreshingLabel.setVisibility(View.GONE);
            mHintContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏刷新界面
     * 当第一次成功获取数据时调用
     */
    protected final void hideRefreshWhenSuccess() {
        mRefreshContainer.setVisibility(View.GONE);
    }

    /**
     * 刷新按钮监听器
     */
    private final View.OnClickListener mOnRefreshClickListener = v -> refreshData();

    private boolean mUserVisible = false;

    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mUserVisible = isVisibleToUser;
        if (mUserVisible && mViewCreated) {
            realLoad();
        }

        onUserVisible(isVisibleToUser);
    }

    protected void onUserVisible(boolean isVisibleToUser) {

    }

    /**
     * 判断是否已经加载
     */
    private boolean mLoaded = false;

    /**
     * 控制只允许加载一次
     */
    private void realLoad() {
        if (mLoaded) {
            return;
        }

        mLoaded = true;
        onRealViewLoaded(mViewStub.inflate());
    }

    @Override
    public void onDestroyView() {
        mViewCreated = false;
        super.onDestroyView();
    }

    /**
     * 当视图真正加载时调用
     * @param view 真实view
     */
    protected abstract void onRealViewLoaded(View view);
}