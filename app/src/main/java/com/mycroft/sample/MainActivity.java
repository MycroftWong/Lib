package com.mycroft.sample;

import android.Manifest;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mycroft.lib.net.RemoteService;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author wangqiang
 */
public class MainActivity extends AppCompatActivity {

    private final List<String> mMovies = new ArrayList<>();
    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(android.R.layout.simple_list_item_1, mMovies) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(android.R.id.text1, item);
            }
        };
        recyclerView.setAdapter(mAdapter);

        new RxPermissions(this).request(Manifest.permission.INTERNET)
                .subscribe(granted -> loadData());
    }

    private static final String BASE_URL = "http://api.douban.com/";

    private void loadData() {
        RemoteService.init(this, null);
        RemoteService.getImpl().initRetrofit(BASE_URL, null);
        IApiService service = RemoteService.getImpl().createApiService(BASE_URL, IApiService.class);
        service.getDoubanTop(0, 250)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray array = jsonObject.getJSONArray("subjects");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject j = array.getJSONObject(i);
                        mMovies.add(j.getString("title"));
                    }

                    mAdapter.notifyDataSetChanged();
                }, throwable -> ToastUtils.showShort("error"), () -> {
                });

    }

    interface IApiService {
        /**
         * 获取豆瓣250
         *
         * @return
         */
        @GET("v2/movie/top250")
        Observable<String> getDoubanTop(@Query("start") int start, @Query("count") int count);
    }
}
