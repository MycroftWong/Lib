package com.mycroft.lib.base;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mycroft.lib.R;

/**
 * Created by Mycroft_Wong on 2015/12/30.
 *
 * @author mycroft
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        initFields(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(getResId());
        initViews();
        loadData();
    }

    /**
     * 获取布局资源id
     *
     * @return layout id
     */
    protected abstract int getResId();

    /**
     * 进行初始化工作，在super.onCreate(Bundle)之前调用
     *
     * @param savedInstanceState 保存的状态
     */
    protected abstract void initFields(@Nullable Bundle savedInstanceState);

    /**
     * 初始化view, 在{@link android.app.Activity#setContentView(int)}之后调用
     */
    protected abstract void initViews();

    /**
     * 加载数据，在所有初始化完成之后调用
     */
    protected abstract void loadData();

    private Dialog mLoadingDialog;

    /**
     * 显示通用的加载{@link Dialog}, 默认cancelable=false
     */
    protected final void showLoadingDialog() {
        showLoadingDialog(false);
    }

    /**
     * 显示通用的加载{@link Dialog}
     *
     * @param cancelable 是否允许点击空白处取消
     */
    protected final void showLoadingDialog(boolean cancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = createLoadingDialog();
        }
        mLoadingDialog.setCancelable(cancelable);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mLoadingDialog.show();
        } else {
            runOnUiThread(mLoadingDialog::show);
        }
    }

    /**
     * 构造通用的加载{@link Dialog}, 在子类中重写，可以更改样式
     *
     * @return 通用的加载Dialog
     */
    @NonNull
    protected Dialog createLoadingDialog() {
        Dialog dialog = new Dialog(this, R.style.LoadingDialog);
        dialog.setContentView(R.layout.common_dialog);
        return dialog;
    }

    /**
     * 隐藏加载Dialog
     */
    protected final void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                mLoadingDialog.cancel();
            } else {
                runOnUiThread(mLoadingDialog::cancel);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
            }
            mLoadingDialog = null;
        }
        super.onDestroy();
    }
}

