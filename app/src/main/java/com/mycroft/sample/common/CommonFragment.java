package com.mycroft.sample.common;

import android.app.Dialog;

import androidx.annotation.NonNull;

import com.mycroft.lib.base.BaseFragment;
import com.mycroft.sample.R;

/**
 * @author wangqiang
 */
public class CommonFragment extends BaseFragment {

    @NonNull
    @Override
    protected Dialog createLoadingDialog() {
        Dialog dialog = new Dialog(getContext(), R.style.CommonLoadingDialogStyle);
        dialog.setContentView(R.layout.common_loading_dialog);
        return dialog;
    }
}
