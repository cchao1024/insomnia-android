package com.cchao.insomnia.api;

import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.global.GLobalInfo;
import com.cchao.insomnia.manager.UserManager;
import com.cchao.simplelib.core.AndroidHelper;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.util.StringHelper;
import com.cchao.simplelib.util.UrlUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author cchao
 * @version 8/10/18.
 */
public class HttpClientManager {
    private static final String TAG_LOG = "RetrofitHelper";
    private static OkHttpClient mProdHttpClient = null;
    public static final String AUTHORIZATION = "Authorization";

    public static OkHttpClient getProdOkHttpClient() {
        if (mProdHttpClient == null) {
            initHttpClient();
        }
        return mProdHttpClient;
    }

    private static void initHttpClient() {
        mProdHttpClient = new OkHttpClient.Builder()
            .connectTimeout(Constants.Config.TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(Constants.Config.TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(Constants.Config.TIMEOUT, TimeUnit.MILLISECONDS)
            // 添加公共的参数
            .addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    String originUrl = chain.request().url().url().toString();
                    // 加入通用的请求参数
                    Map<String, String> paramsMap = new HashMap<>();
                    paramsMap.put("device_number", AndroidHelper.getDeviceNum());
                    paramsMap.put("appBuild", Constants.Config.API_BUILD);
                    originUrl = UrlUtil.buildUrl(originUrl, paramsMap);
                    // 构建request
                    request = request.newBuilder().url(originUrl)
                        .addHeader(AUTHORIZATION, UserManager.getToken())
                        .build();
                    // 事件收集
                    Logs.logEvent("发起请求：", "请求Url :【" + request.url() + "】");
                    //收集请求体
                    if (request.body() instanceof FormBody) {
                        FormBody formBody = ((FormBody) request.body());
                        String postBody = "";
                        for (int i = 0; i < formBody.size(); i++) {
                            postBody += formBody.encodedName(i) + " = " + formBody.encodedValue(i) + " & ";
                        }
                        if (StringHelper.isNotEmpty(postBody)) {
                            Logs.logEvent("请求体：", postBody);
                        }
                    }
                    return chain.proceed(request);
                }
            })
            .build();

        if (GLobalInfo.isDebug()) {
        }
    }


}
