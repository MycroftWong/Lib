package com.mycroft.sample.net;

import com.mycroft.sample.model.ArticleListModel;
import com.mycroft.sample.model.Category;
import com.mycroft.sample.model.OfficialAccount;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author mycroft
 */
public interface IApiService {

    /**
     * 首页文章列表
     *
     * @param page page
     * @return
     */
    @GET("article/list/{page}/json")
    Observable<NetModel<ArticleListModel>> getArticleList(@Path("page") int page);

    /**
     * 知识体系
     *
     * @return
     */
//    @Headers(HttpConstants.CACHE_CONTROL + ": " + HttpConstants.FORCE_CONTROL)
    @GET("tree/json")
    Observable<NetModel<List<Category>>> getCategoryList();

    @GET("chapters/json")
    Observable<NetModel<List<OfficialAccount>>> getOfficialAccountList();
}
