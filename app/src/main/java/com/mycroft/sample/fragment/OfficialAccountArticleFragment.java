package com.mycroft.sample.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.mycroft.sample.common.CommonFragment;
import com.mycroft.sample.model.OfficialAccount;

public class OfficialAccountArticleFragment extends CommonFragment {

    private static final String ARGS_OFFICIAL_ACCOUNT = "official_account.args";

    public static OfficialAccountArticleFragment newInstance(@NonNull OfficialAccount officialAccount) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_OFFICIAL_ACCOUNT, officialAccount);

        OfficialAccountArticleFragment fragment = new OfficialAccountArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
