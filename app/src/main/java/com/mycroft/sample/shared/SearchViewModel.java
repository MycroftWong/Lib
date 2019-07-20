package com.mycroft.sample.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 用于搜索页面
 *
 * @author mycroft
 */
public final class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> searchKey = new MutableLiveData<>();

    public LiveData<String> getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey.setValue(searchKey);
    }
}
