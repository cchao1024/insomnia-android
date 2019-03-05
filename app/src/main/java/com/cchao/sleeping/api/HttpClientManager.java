package com.cchao.insomnia.api;

import android.text.TextUtils;

import com.cchao.simplelib.util.DeviceInfo;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplelib.util.StringHelper;
import com.cchao.simplelib.util.UrlUtil;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.global.GLobalInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author cchao
 * @version 8/10/18.
 */
public class HttpClientManager {
    private static final String TAG_LOG = "RetrofitHelper";
    private static OkHttpClient mProdHttpClient = null;
    static final Charset UTF8 = Charset.forName("UTF-8");

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
            //拦截器添加公共的参数
            .addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    String originUrl = chain.request().url().url().toString();
                    // 加入通用的请求参数
                    Map<String, String> paramsMap = new HashMap<>();
                    paramsMap.put("device_number", DeviceInfo.getDeviceNum());
                    paramsMap.put("appBuild", Constants.Config.API_BUILD);
                    originUrl = UrlUtil.buildUrl(originUrl, paramsMap);
                    // 构建request
                    request = request.newBuilder().url(originUrl).build();
                    // 事件收集
                    ExceptionCollect.logEvent("发起网络请求："
                        , "请求Url : 【" + request.url() + "】");
                    //收集请求体
                    if (request.body() instanceof FormBody) {
                        FormBody formBody = ((FormBody) request.body());
                        String postBody = "";
                        for (int i = 0; i < formBody.size(); i++) {
                            postBody += formBody.encodedName(i) + " = " + formBody.encodedValue(i) + " & ";
                        }
                        if (StringHelper.isNotEmpty(postBody)) {
                            ExceptionCollect.logEvent("请求体：", postBody);
                        }
                    }
                    return chain.proceed(request);
                }
            })
            // 收集后端返回的异常数据
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    try {
                        String url = request.url().url().toString();

                        if (response.isSuccessful()) {
                            // 获取响应体buffer clone写入
                            BufferedSource source = response.body().source();
                            source.request(Long.MAX_VALUE); // Buffer the entire body.
                            Buffer buffer = source.buffer();

                            String json = buffer.clone().readString(UTF8).trim();
                            try {
                                if (TextUtils.isEmpty(json)) {
                                    ExceptionCollect.logException(Constants.ApiResp.Json_Empty + "url : " + url);
                                }
                                new JSONObject(json);
                            } catch (JSONException e) {
                                ExceptionCollect.logException(Constants.ApiResp.Json_Fail +
                                    "url : " + url + " json : " + json + " exception " + e.getMessage());
                            } catch (Exception e) {
                                ExceptionCollect.logException(e);
                            }
                        } else if (response.code() == 500) {
                            ExceptionCollect.logException("接口返回了500" + "url : " + url);
                        } else if (response.code() == 503) {
                            ExceptionCollect.logException("接口返回了503" + "url : " + url);
                        }
                    } catch (Exception e) {
                        ExceptionCollect.logException(e);
                    }
                    return response;
                }
            })
            .build();

        if (GLobalInfo.isDebug()) {
        }
    }


}
