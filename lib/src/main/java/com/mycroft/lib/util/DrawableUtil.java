package com.mycroft.lib.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

/**
 * Created by Mycroft on 2016/6/6.
 */
public final class DrawableUtil {

    private DrawableUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void setTintDrawable(@NonNull Context context, @NonNull ImageView imageView, int drawableRes, int idleColor, int pressedColor) {
        final Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
        final int[] colors = new int[]{idleColor, pressedColor};
        final int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};

        // 构造StateListDrawable
        final StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(states[0], drawable); // 注意顺序, 必须把 pressed 放在前面，否则没有效果
        stateListDrawable.addState(states[1], drawable);

        final Drawable.ConstantState state = stateListDrawable.getConstantState();
        final Drawable result = DrawableCompat.wrap(state == null ? stateListDrawable : state.newDrawable()).mutate();

        DrawableCompat.setTintList(result, new ColorStateList(states, colors));
        imageView.setImageDrawable(result);
    }

}
