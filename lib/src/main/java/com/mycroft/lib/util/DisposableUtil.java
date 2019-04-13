package com.mycroft.lib.util;

import io.reactivex.disposables.Disposable;

/**
 * Created by Mycroft_Wong on 2016/1/19.
 */
public final class DisposableUtil {

    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
