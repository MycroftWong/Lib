package com.mycroft.sample.common;

import android.app.Dialog;

import androidx.annotation.NonNull;

import com.mycroft.lib.base.BaseActivity;
import com.mycroft.sample.R;

/**
 * @author mycroft
 */
public abstract class CommonActivity extends BaseActivity {

    @NonNull
    @Override
    protected Dialog createLoadingDialog() {
        Dialog dialog = new Dialog(this, R.style.CommonLoadingDialogStyle);
        dialog.setContentView(R.layout.common_loading_dialog);
        return dialog;
    }
}
