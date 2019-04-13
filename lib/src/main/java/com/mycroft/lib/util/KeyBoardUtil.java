package com.mycroft.lib.util;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Mycroft_Wong on 2015/12/30.
 */
public final class KeyBoardUtil {

    private KeyBoardUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 打开软键盘
     *
     * @param editText 输入框
     * @param context  上下文
     */
    public static void openKeyboard(@NonNull Context context, @NonNull EditText editText) {
        final InputMethodManager imm = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param editText 输入框
     * @param context  上下文
     */
    public static void closeKeyboard(@NonNull Context context, @NonNull EditText editText) {
        final InputMethodManager imm = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static boolean isKeyboardOpen(@NonNull Context context) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isAcceptingText();
    }
}
