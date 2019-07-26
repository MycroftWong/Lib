package com.mycroft.lib.util;

import androidx.annotation.IdRes;
import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * fragment的切换帮助类
 *
 * @author mycroft
 */
public final class FragmentSwitcher {

    private final FragmentManager fragmentManager;
    @IdRes
    private int containerId;

    private final FragmentAdapter adapter;

    public FragmentSwitcher(FragmentManager fragmentManager, @IdRes int container, FragmentAdapter adapter) {
        this.fragmentManager = fragmentManager;
        containerId = container;
        this.adapter = adapter;
    }

    private final ArrayMap<Integer, Fragment.SavedState> savedStateArrayMap = new ArrayMap<>();

    private Fragment currentFragment;
    private int currentPosition;

    private final ArrayList<Integer> saveStatePosition = new ArrayList<>();

    public void setSaveStatePosition(Integer... saveStatePosition) {
        this.saveStatePosition.addAll(Arrays.asList(saveStatePosition));
    }

    public void startFragment(int pos) {
        // 下面是一连串的处理fragment切换的代码
        FragmentTransaction ft = fragmentManager
                .beginTransaction();

        Fragment fragment = fragmentManager.findFragmentByTag(makeFragmentName(containerId, pos));

        if (fragment == null) {
            fragment = adapter.getFragmentBy(pos);

            if (currentFragment != null) {
                ft.hide(currentFragment)
                        .setMaxLifecycle(currentFragment, Lifecycle.State.STARTED);
            }

            Fragment.SavedState fss = savedStateArrayMap.get(pos);
            if (fss != null) {
                fragment.setInitialSavedState(fss);
            }
            ft.add(containerId, fragment, makeFragmentName(containerId, pos))
                    .setMaxLifecycle(fragment, Lifecycle.State.RESUMED);

            currentFragment = fragment;
        } else {
            if (fragment != currentFragment) {
                ft.show(fragment)
                        .setMaxLifecycle(fragment, Lifecycle.State.RESUMED);

                if (currentFragment != null) {
                    if (saveStatePosition.contains(currentPosition)) {
                        savedStateArrayMap.put(currentPosition,
                                currentFragment.isAdded() ? fragmentManager.saveFragmentInstanceState(currentFragment) : null);

                        ft.remove(currentFragment);
                    } else {
                        ft.hide(currentFragment)
                                .setMaxLifecycle(currentFragment, Lifecycle.State.STARTED);
                    }
                }
                currentFragment = fragment;
            }
        }
        currentPosition = pos;
        ft.commitAllowingStateLoss();
    }

    /**
     * 清理资源
     */
    public void destroy() {
        List<Fragment> fragmentList = fragmentManager.getFragments();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment item : fragmentList) {
            transaction.detach(item);
        }

        transaction.commitAllowingStateLoss();
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    public interface FragmentAdapter {
        /**
         * 根据position 提供 fragment
         *
         * @param position
         * @return
         */
        Fragment getFragmentBy(int position);
    }

}
