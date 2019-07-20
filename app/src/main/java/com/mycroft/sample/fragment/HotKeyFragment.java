package com.mycroft.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ToastUtils;
import com.donkingliang.labels.LabelsView;
import com.mycroft.sample.R;
import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.model.HotKey;
import com.mycroft.sample.net.NetService;
import com.mycroft.sample.shared.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 热门搜索词
 *
 * @author mycroft
 */
public class HotKeyFragment extends CommonFragment {

    private SearchViewModel searchViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot_key, container, false);
    }

    private final List<HotKey> hotKeyList = new ArrayList<>();

    private TextView hotKeyLabel;
    private LabelsView labelsView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hotKeyLabel = view.findViewById(R.id.hotKeyLabel);
        labelsView = view.findViewById(R.id.labelsView);

        labelsView.setSelectType(LabelsView.SelectType.NONE);
        labelsView.setOnLabelClickListener((label, data, position) ->
                searchViewModel.setSearchKey(hotKeyList.get(position).getName()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hotKeyList.isEmpty()) {
            loadData();
        } else {
            showHotKey();
        }
    }

    private Disposable disposable;

    private void loadData() {
        if (disposable != null) {
            return;
        }

        disposable = NetService.getInstance().getHotKeyList()
                .subscribe(hotKeys -> {
                            hotKeyList.addAll(hotKeys);
                            showHotKey();
                        }, throwable -> ToastUtils.showShort(throwable.getMessage()),
                        () -> disposable = null);
    }

    private void showHotKey() {
        hotKeyLabel.setVisibility(View.VISIBLE);
        labelsView.setLabels(hotKeyList, (label, position, data) -> data.getName());
    }
}
