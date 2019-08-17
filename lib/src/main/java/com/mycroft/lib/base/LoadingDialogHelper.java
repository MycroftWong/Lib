package com.mycroft.lib.base;

import android.app.Dialog;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import static com.blankj.utilcode.util.Utils.runOnUiThread;

/**
 * 加载dialog的帮助类
 *
 * @author mycroft
 */
public final class LoadingDialogHelper implements DefaultLifecycleObserver {

    private DialogCreator dialogCreator;

    public LoadingDialogHelper(@NonNull LifecycleOwner lifecycleOwner, @NonNull DialogCreator dialogCreator) {
        lifecycleOwner.getLifecycle().addObserver(this);
        this.dialogCreator = dialogCreator;
    }

    private Dialog mLoadingDialog;

    /**
     * 显示通用的加载{@link Dialog}, 默认cancelable=false
     */
    public final void showLoadingDialog() {
        showLoadingDialog(false);
    }

    /**
     * 显示通用的加载{@link Dialog}
     *
     * @param cancelable 是否允许点击空白处取消
     */
    public final void showLoadingDialog(boolean cancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = dialogCreator.createLoadingDialog();
        }
        mLoadingDialog.setCancelable(cancelable);

        if (Looper.myLooper() == Looper.getMainLooper()) {
            mLoadingDialog.show();
        } else {
            runOnUiThread(mLoadingDialog::show);
        }
    }

    /**
     * 隐藏加载Dialog
     */
    public final void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                mLoadingDialog.cancel();
            } else {
                runOnUiThread(mLoadingDialog::cancel);
            }
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
            }
            mLoadingDialog = null;
        }
    }

    public interface DialogCreator {
        /**
         * create loading dialog
         *
         * @return dialog
         */
        Dialog createLoadingDialog();
    }
}
