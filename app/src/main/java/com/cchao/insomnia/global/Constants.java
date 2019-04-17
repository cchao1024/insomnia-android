package com.cchao.insomnia.global;

import com.cchao.insomnia.BuildConfig;

/**
 * 常量
 */
public abstract class Constants {
    public static final int SHAKE_INTERVAL = 600; //防用户抖动间隔
    public static final String TEST_IMAGE_PATH = "http://d6.yihaodianimg.com/V00/M00/3E/5C/CgQDslSNDEyAQp-mAAHoVWDzhu877700_380x380.jpg";

    public interface Config {

        String API_Host = "http://192.168.0.104:8080/"; //接口API版本
        String API_BUILD = String.valueOf(BuildConfig.VERSION_CODE); //接口API版本

        boolean SUPPORT_THUMBNAIL = false; //是否支持缩略图(使用最高清的图片)

        int TIMEOUT = 60 * 1000; // 网络访问超时时间

        String PHONE = "+00852-21364966";
        String LIVE_CHAT_NUMBER = "7243681";
        int LIVE_CHAT_GROUP = 6;
        int PAGE_SIZE = 30;
    }


    public interface Event {
        int Update_Play_Status = 61;

    }

    public interface Prefs {
        String USER_INFO = "user_info";
        String USER_NAME = "user_name";
        String USER_EMAIL = "user_email";
        String LOGIN_TIPS = "login_tips";
        String MUSIC_PLAY_MODE = "music_play_mode";
        String WISH_LIST = "wish_list";
    }

    // 后端响应Code
    public interface ApiResp {
        String CODE_SUC = "00";
        String CODE = "code";

        String Json_Empty = "json_empty ";
        String Json_Fail = "json_fail ";
        String Resp_500 = "Resp_500 ";
        String Resp_503 = "Resp_503 ";

    }

    public interface Extra {
        String IMAGE_URL = "image_url";
        String ID = "id";
    }
}
