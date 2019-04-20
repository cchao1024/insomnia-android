package com.cchao.insomnia.api;


import com.cchao.insomnia.model.javabean.RespBean;
import com.cchao.insomnia.model.javabean.RespListBean;
import com.cchao.insomnia.model.javabean.fall.FallImage;
import com.cchao.insomnia.model.javabean.fall.FallIndex;
import com.cchao.insomnia.model.javabean.fall.FallMusic;
import com.cchao.insomnia.model.javabean.global.AppLaunch;
import com.cchao.insomnia.model.javabean.post.PostListVO;
import com.cchao.insomnia.model.javabean.post.PostVO;
import com.cchao.insomnia.model.javabean.user.UserBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * description
 * author  cchao
 * date  2017/2/24
 **/
public interface Apis {

    @GET("/postbox/app/getLaunch")
    Observable<RespBean<AppLaunch>> appLaunch();

    /**
     * 忘记密码 必选参数: email
     */
    @FormUrlEncoded
    @POST("?com=customer&t=findPwdByEmail")
    Observable<RespBean> resetPwdByEmail(@Field("email") String email);

    @FormUrlEncoded
    @POST("/user/{path}")
    Observable<RespBean<UserBean>> login(@Path("path") String path, @Field("email") String email
        , @Field("password") String pwd);


    @GET("/fallIndex")
    Observable<RespBean<FallIndex>> getIndexData();

    @FormUrlEncoded
    @POST("/fallimage/getByPage")
    Observable<RespListBean<FallImage>> getImageList(@Field("page") int page);

    @FormUrlEncoded
    @POST("/postbox/post/list")
    Observable<RespListBean<PostListVO>> getPostBoxList(@Field("page") int page);

    @FormUrlEncoded
    @POST("/postbox/post/detail")
    Observable<RespBean<PostVO>> getPostDetail(@Field("id") long id);

    @FormUrlEncoded
    @POST("/postbox/post/new")
    Observable<RespBean> addPost(@Field("content") String content,@Field("images") String images);

    @FormUrlEncoded
    @POST("/postbox/{type}/new")
    Observable<RespBean> addCommentOrReply(@Path("type") String type
        ,@Field("content") String content,@Field("images") String images);

    @FormUrlEncoded
    @POST("/fallmusic/getByPage")
    Observable<RespListBean<FallMusic>> getMusicList(@Field("page") int page);

    @FormUrlEncoded
    @POST("/fallmusic/play_count")
    Observable<RespBean> playCount(@Field("id") String id);
}
