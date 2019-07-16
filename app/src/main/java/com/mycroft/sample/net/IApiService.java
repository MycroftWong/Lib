package com.mycroft.sample.net;

import com.mycroft.sample.model.ArticleListModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author mycroft
 */
public interface IApiService {

    @GET("article/list/{page}/json")
    Observable<NetModel<ArticleListModel>> getArticleList(@Path("page") int page);
}
