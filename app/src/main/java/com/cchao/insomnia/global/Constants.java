package com.cchao.insomnia.global;

import com.cchao.insomnia.BuildConfig;

/**
 * 常量
 */
public class Constants {
    // 防用户抖动间隔
    public static final int SHAKE_INTERVAL = 600;
    public static final String TEST_IMAGE_PATH = "http://d6.yihaodianimg.com/V00/M00/3E/5C/CgQDslSNDEyAQp-mAAHoVWDzhu877700_380x380.jpg";

    public interface RequestCode {
        int TAKE_IMAGE = 333;
    }

    public interface Config {

        String API_Host = "http://127.0.0.1:8080/";

        // 接口API版本
        String API_BUILD = String.valueOf(BuildConfig.VERSION_CODE);

        // 网络访问超时时间
        int TIMEOUT = 60 * 1000;

        int PAGE_SIZE = 20;

        int MAX_POST_IMAGE=5;
    }

    public interface Type {
        int FALL_IMAGE = 1;
        int FALL_MUSIC = 2;
        int UP_IMAGE = 3;
        int UP_MUSIC = 4;
    }

    public interface Event {
        int Update_Play_Status = 61;
        int Update_Post_Box = 10001;
        int update_count_down = 1002;
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

    }

    public interface Extra {
        String IMAGE_URL = "image_url";
        String ID = "toId";
    }

    public interface Drawer {
        int Divider = 1111;
        int Wish = 1001;
        int FeedBack = 1002;
        int AboutUs = 1003;
        int TimeDown = 1004;
        int Settings = 1005;
    }

    /**
     * api 追加的通用参数
     */
    public interface Api_Appand {
        String App_Build = "appBuild";
        String Device_No = "deviceNo";
        String Page_Size = "pageSize";
    }
}
