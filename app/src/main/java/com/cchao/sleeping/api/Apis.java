package com.cchao.sleeping.api;


import com.cchao.sleeping.model.javabean.RespBean;
import com.cchao.sleeping.model.javabean.RespListBean;
import com.cchao.sleeping.model.javabean.fall.FallImage;
import com.cchao.sleeping.model.javabean.fall.FallIndex;
import com.cchao.sleeping.model.javabean.fall.FallMusic;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * description
 * author  cchao
 * date  2017/2/24
 **/
public interface Apis {

    /**
     * 忘记密码 必选参数: email
     */
    @FormUrlEncoded
    @POST("?com=customer&t=findPwdByEmail")
    Observable<RespBean> resetPwdByEmail(@Field("email") String email);

    @GET("/fallIndex")
    Observable<RespBean<FallIndex>> getIndexData();

    @FormUrlEncoded
    @POST("/fallimage/getByPage")
    Observable<RespListBean<FallImage>> getImageList(@Field("page") int page);

    @FormUrlEncoded
    @POST("/fallmusic/getByPage")
    Observable<RespListBean<FallMusic>> getMusicList(@Field("page") int page);

    @FormUrlEncoded
    @POST("/fallmusic/play_count")
    Observable<RespBean> playCount(@Field("id") String id);
}
