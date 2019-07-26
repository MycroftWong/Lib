package com.mycroft.sample.net;

import com.mycroft.sample.model.Article;
import com.mycroft.sample.model.Category;
import com.mycroft.sample.model.HotKey;
import com.mycroft.sample.model.ListData;
import com.mycroft.sample.model.OfficialAccount;
import com.mycroft.sample.model.Project;
import com.mycroft.sample.model.Tools;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * @author mycroft
 */
public interface IApiService {

    /**
     * 知识体系
     *
     * @return
     */
    @GET("tree/json")
    Observable<NetModel<List<Category>>> getCategoryList();

    /**
     * 获取微信公众号
     *
     * @return
     */
    @GET("wxarticle/chapters/json")
    Observable<NetModel<List<OfficialAccount>>> getOfficialAccountList();

    /**
     * 通用的获取文章列表的接口
     *
     * @param url url
     * @return
     */
    @GET
    Observable<NetModel<ListData<Article>>> getArticleList(@Url String url);

    /**
     * 获取导航数据
     *
     * @return
     */
    @GET("navi/json")
    Observable<NetModel<List<Tools>>> getToolList();

    /**
     * 获取项目分类
     *
     * @return
     */
    @GET("project/tree/json")
    Observable<NetModel<List<Project>>> getProjectList();

    /**
     * 热门搜索词
     *
     * @return
     */
    @GET("/hotkey/json")
    Observable<NetModel<List<HotKey>>> getHotKeyList();

    /**
     * 搜索
     *
     * @param key  搜索关键字
     * @param page page start with 0
     * @return
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    Observable<NetModel<ListData<Article>>> search(@Field("k") String key, @Path("page") int page);
}
