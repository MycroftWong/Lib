package com.mycroft.sample.net;

import com.mycroft.sample.model.ArticleListModel;
import com.mycroft.sample.model.Category;
import com.mycroft.sample.model.OfficialAccount;
import com.mycroft.sample.model.Project;
import com.mycroft.sample.model.Tools;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
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

    @GET("wxarticle/chapters/json")
    Observable<NetModel<List<OfficialAccount>>> getOfficialAccountList();

    /**
     * 通用的获取文章列表的接口
     *
     * @param url url
     * @return
     */
    @GET
    Observable<NetModel<ArticleListModel>> getArticleList(@Url String url);

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
}
