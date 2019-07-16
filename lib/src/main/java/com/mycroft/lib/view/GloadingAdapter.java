package com.mycroft.lib.view;

import android.view.View;

import com.billy.android.loading.Gloading;

/**
 * 统一加载adapter
 *
 * @author mycroft
 */
public class GloadingAdapter implements Gloading.Adapter {
    @Override
    public View getView(Gloading.Holder holder, View convertView, int status) {

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
