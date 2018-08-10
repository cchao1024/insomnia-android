package com.cchao.sleeping.api;


import com.cchao.sleeping.model.javabean.RespBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
}
