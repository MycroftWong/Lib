package com.mycroft.sample.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.blankj.swipepanel.SwipePanel;
import com.blankj.utilcode.util.SizeUtils;
import com.github.ybq.android.spinkit.SpinKitView;
import com.mycroft.sample.R;

import static com.mycroft.lib.view.Loading.STATUS_EMPTY_DATA;
import static com.mycroft.lib.view.Loading.STATUS_LOADING;
import static com.mycroft.lib.view.Loading.STATUS_LOAD_FAILED;
import static com.mycroft.lib.view.Loading.STATUS_LOAD_SUCCESS;

/**
 * @author mycroft
 */
@SuppressLint("ViewConstructor")
final class StatusView2 extends SwipePanel {

    private SpinKitView spinKitView;
    private TextView statusText;
    private TextView retryButton;

    public StatusView2(@NonNull Context context, @NonNull Runnable retryTask) {
        super(context);

        setLeftCenter(false);
        setLeftEdgeSize(SizeUtils.dp2px(100));
        setLeftSwipeColor(ContextCompat.getColor(context, R.color.colorPrimary));

        LayoutInflater.from(context).inflate(R.layout.loading_status_view2, this, true);
        spinKitView = findViewById(R.id.spinKitView);
        statusText = findViewById(R.id.statusText);
        retryButton = findViewById(R.id.retryButton);

        retryButton.setOnClickListener(v -> retryTask.run());
    }

    public void setStatus(int status) {
        switch (status) {
            case STATUS_LOAD_SUCCESS:
                setVisibility(GONE);
                onSuccess();
                break;
            case STATUS_LOADING:
                onLoading();
                break;
            case STATUS_LOAD_FAILED:
                onLoadingFailed();
                break;
            case STATUS_EMPTY_DATA:
                onEmpty();
                break;
            default:
                break;
        }
    }

    private void onEmpty() {
        // 2019/4/30 修改
        spinKitView.setVisibility(GONE);
        statusText.setVisibility(VISIBLE);
        retryButton.setVisibility(GONE);

        statusText.setText(R.string.text_load_empty);
        retryButton.setEnabled(false);
    }

    private void onLoadingFailed() {
        spinKitView.setVisibility(GONE);
        statusText.setVisibility(VISIBLE);
        retryButton.setVisibility(VISIBLE);

        statusText.setText(R.string.text_load_failed);
        retryButton.setEnabled(true);
    }

    private void onSuccess() {
        // nothing
    }

    private void onLoading() {
        spinKitView.setVisibility(VISIBLE);
        statusText.setVisibility(GONE);
        retryButton.setVisibility(GONE);

        statusText.setText(R.string.text_loading);
        retryButton.setEnabled(false);
    }
}
