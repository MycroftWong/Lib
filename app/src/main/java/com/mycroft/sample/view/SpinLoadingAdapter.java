package com.mycroft.sample.view;

import android.view.View;

import com.mycroft.lib.view.LoadingAdapter;
import com.mycroft.lib.view.LoadingHolder;

public class SpinLoadingAdapter implements LoadingAdapter {
    @Override
    public View getView(LoadingHolder holder, View convertView, int status) {

        StatusView statusView = null;

        if (convertView instanceof StatusView) {
            statusView = (StatusView) convertView;
        }

        if (statusView == null) {
            statusView = new StatusView(holder.getContext(), holder.getRetryTask());
        }

        statusView.setStatus(status);

        return statusView;
    }
}

