package com.mycroft.lib.util;

import io.reactivex.disposables.Disposable;

/**
 * Created by Mycroft_Wong on 2016/1/19.
 */
public final class DisposableUtil {

    /**
     * 关闭所有的事件传递
     *
     * @param disposables 代替发送的事件流
     */
    public static void dispose(Disposable... disposables) {
        if (disposables == null || disposables.length == 0) {
            return;
        }
        for (Disposable d : disposables) {
            if (d != null && !d.isDisposed()) {
                d.dispose();
            }
        }

    }
}
