package com.mycroft.lib.util;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * fragment的切换帮助类，有bug需要处理
 *
 * @author mycroft
 */
@Deprecated
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

    /**
     * @param saveStatePosition 不保留{@link Fragment}实例，而是保留{@link Fragment.SavedState}的位置
     */
    public void setSaveStatePosition(Integer... saveStatePosition) {
        this.saveStatePosition.addAll(Arrays.asList(saveStatePosition));
    }

    public void startFragment(int pos) {
        // 下面是一连串的处理fragment切换的代码
        FragmentTransaction ft = fragmentManager
                .beginTransaction();

        Fragment fragment = createFragment(ft, pos);
        setPrimaryFragment(ft, fragment);
//
////        Fragment fragment = fragmentManager.findFragmentByTag(makeFragmentName(containerId, pos));
//
//        if (fragment == null) {
//            // if not exists, create it
//            fragment = adapter.getFragmentBy(pos);
//
//            ft.add(containerId, fragment, makeFragmentName(containerId, pos))
//                    .setMaxLifecycle(fragment, Lifecycle.State.RESUMED);
//
//            if (currentFragment != null) {
//                ft.hide(currentFragment)
//                        .setMaxLifecycle(currentFragment, Lifecycle.State.STARTED);
//            }
//
////            Fragment.SavedState fss = savedStateArrayMap.get(pos);
////            if (fss != null) {
////                fragment.setInitialSavedState(fss);
////            }
//
//            currentFragment = fragment;
//        } else {
//            ft.attach(fragment);
//
//            if (fragment != currentFragment) {
//                ft.show(fragment)
//                        .setMaxLifecycle(fragment, Lifecycle.State.RESUMED);
//
////                if (currentFragment != null) {
////                    if (saveStatePosition.contains(currentPosition)) {
////                        savedStateArrayMap.put(currentPosition,
////                                currentFragment.isAdded() ? fragmentManager.saveFragmentInstanceState(currentFragment) : null);
////
////                        ft.remove(currentFragment);
////                    } else {
////                        ft.hide(currentFragment)
////                                .setMaxLifecycle(currentFragment, Lifecycle.State.STARTED);
////                    }
////                }
//            }
//        }
//
//        if (fragment != currentFragment) {
//            fragment.setMenuVisibility(false);
//            ft.setMaxLifecycle(fragment, Lifecycle.State.STARTED);
//        }
//
//        currentFragment = fragment;

        currentPosition = pos;
        ft.commitAllowingStateLoss();
    }

    /**
     * @param fragmentTransaction fragment transaction
     * @param position            position
     * @return fragment of position
     */
    private Fragment createFragment(@NonNull FragmentTransaction fragmentTransaction, int position) {
        String name = makeFragmentName(containerId, position);
        Fragment fragment = fragmentManager.findFragmentByTag(name);

        if (fragment == null) {
            fragment = adapter.getFragmentBy(position);
            fragmentTransaction.add(containerId, fragment, name);
            fragmentTransaction.attach(fragment);
        }

        return fragment;
    }

    /**
     * set primary fragment
     *
     * @param fragmentTransaction fragment transaction
     * @param fragment            fragment
     */
    private void setPrimaryFragment(@NonNull FragmentTransaction fragmentTransaction, Fragment fragment) {
        if (fragment != currentFragment) {
            if (currentFragment != null) {
                currentFragment.setMenuVisibility(false);
                fragmentTransaction.setMaxLifecycle(currentFragment, Lifecycle.State.CREATED);
            }
            fragment.setMenuVisibility(true);
            fragmentTransaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED);

            currentFragment = fragment;
        }
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
