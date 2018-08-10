package com.cchao.sleeping.api;

import com.cchao.sleeping.global.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit 网络请求helper
 * Created by cchao on 2017.
 */
public class RetrofitHelper {

    private Apis mApiService = null;
    private static RetrofitHelper mRetrofit;

    private RetrofitHelper() {
    }

    static {
        getDefault();
    }

    /**
     * 获取默认正式环境 retrofit
     */
    public static RetrofitHelper getDefault() {
        if (mRetrofit == null) {
            mRetrofit = new RetrofitHelper();
            mRetrofit.initApiService(Constants.Config.API_Host, Apis.class);
        }
        return mRetrofit;
    }


    private void initApiService(String baseUrl, Class clz) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(HttpClientManager.getProdOkHttpClient())
            .validateEagerly(true)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

        mApiService = (Apis) retrofit.create(clz);
    }

    public Apis getApiService() {
        if (mApiService != null) {
            return mApiService;
        } else {
            throw new IllegalArgumentException("mApiService is null");
        }
    }

    public static Apis getApis() {
        if (getDefault().mApiService != null) {
            return getDefault().mApiService;
        } else {
            throw new IllegalArgumentException("mApiService is null");
        }
    }
}
