package com.mycroft.lib.base;

import android.app.Dialog;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mycroft.lib.R;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author Mycroft
 * @date 2016/6/13
 */
public abstract class BaseFragment extends Fragment {

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
            AndroidSchedulers.mainThread().scheduleDirect(mLoadingDialog::show);
        }
    }

    /**
     * 构造通用的加载{@link Dialog}, 在子类中重写，可以更改样式
     *
     * @return 通用的加载Dialog
     */
    @NonNull
    protected Dialog createLoadingDialog() {
        Dialog dialog = new Dialog(getContext(), R.style.LoadingDialog);
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
                AndroidSchedulers.mainThread().scheduleDirect(mLoadingDialog::cancel);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
            }
            mLoadingDialog = null;
        }
        super.onDestroyView();
    }

}
