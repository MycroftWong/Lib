package com.mycroft.lib.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Mycroft on 2016/6/13.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFields(savedInstanceState);
    }

    protected abstract void initFields(@Nullable Bundle savedInstanceState);
}
