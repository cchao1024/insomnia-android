package com.cchao.sleeping.global;

import com.cchao.sleeping.BuildConfig;

/**
 * 常量
 */
public abstract class Constants {
    public static final int SHAKE_INTERVAL = 600; //防用户抖动间隔
    public static final String TEST_IMAGE_PATH = "http://d6.yihaodianimg.com/V00/M00/3E/5C/CgQDslSNDEyAQp-mAAHoVWDzhu877700_380x380.jpg";

    public interface Config {

        String API_Host = "http://192.168.0.104:8080"; //接口API版本
        String API_BUILD = String.valueOf(BuildConfig.VERSION_CODE); //接口API版本

        boolean SUPPORT_THUMBNAIL = false; //是否支持缩略图(使用最高清的图片)

        int TIMEOUT = 10 * 1000; // 网络访问超时时间

        String PHONE = "+00852-21364966";
        String LIVE_CHAT_NUMBER = "7243681";
        int LIVE_CHAT_GROUP = 6;
        int PAGE_SIZE = 30;

        String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.yoinsapp";
    }

    public interface Prefs {
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

    public interface Event {
        int Update_Play_Status = 61;

    }

    public static class Extra {

        public static final String PRODUCT_ID = "com.yoinsapp.ProductId";
        public static final String PRODUCT_INFO = "com.yoinsapp.ProductInfo";
        public static final String PRODUCT_NAME = "com.yoinsapp.ProductName";
        public static final String PRODUCT_QUANTITY = "com.yoinsapp.ProductQuantity";
        public static final String PRODUCT_SIZE_CHART = "com.yoinsapp.ProductSizeChart";
        public static final String PRODUCT_ATTR_LIST = "com.yoinsapp.ProductAttrList";
        public static final String PRODUCT_ATTR_VALUES = "com.yoinsapp.ProductAttrValues";
        public static final String PRODUCT_SELECTED_ATTR_LIST = "com.yoinsapp.SelectedAttrList";
        public static final String PRODUCT_BUY_NOW = "com.yoinsapp.BuyNow";
        public static final String PRODUCT_IN_WISH = "com.yoinsapp.Product.InWish";
        public static final String PRODUCT_WISH_NUMS = "com.yoinsapp.Product.WishNums";
        public static final String PRODUCT_LIST = "com.yoinsapp.ProductList";
        public static final String PRODUCT_CID = "com.yoinsapp.Product.cid";
        public static final String PRODUCT_PREVIEW = "com.yoinsapp.ProductPreview";

        public static final String DISCOVER_ID = "com.yoinsapp.DiscoverId";
        public static final String DISCOVER_LIKES = "com.yoinsapp.DiscoverLikes";
        public static final String SHOW_SIZE_ID = "com.yoinsapp.isShowSize";

        public static final String COUNTRY_ID = "com.yoinsapp.CountryId";
        public static final String COUNTRY_NAME = "com.yoinsapp.CountryName";
        public static final String COUNTRY_ABBR = "com.yoinsapp.country_image_url";

        public static final String ZONE_ID = "com.yoinsapp.ZoneId";
        public static final String ZONE_NAME = "com.yoinsapp.ZoneName";
        public static final String E_MAIL = "com.yoinsapp.Email";
        public static final String COME_FROM = "com.yoinsapp.ComeFrom";

        public static final String REFRESH_ADDRESS_LIST = "com.yoinsapp.RefreshAddressList";
        public static final String EDIT_ADDRESS = "com.yoinsapp.EditAddress";
        public static final String DEFAULT_ADDRESS = "com.yoinsapp.IsDefaultAddress";
        public static final String CHECK_ADDRESS = "com.yoinsapp.IsCheckAddress";
        public static final String FROM_CHECKOUT = "com.yoinsapp.from_checkout";
        public static final String CHOOSE_ADDRESS = "com.yoinsapp.ChooseAddress";

        public static final String UPDATE_PROFILE = "com.yoinsapp.UpdateProfile";
        public static final String RELOGIN = "com.yoinsapp.ReLogin";
        public static final String LOGIN = "com.yoinsapp.Login";
        public static final String LOGOUT = "com.yoinsapp.Logout";
        public static final String SIGN_UP = "com.yoinsapp.SignUp";
        public static final String SIGN_IN_DEFER_DEEPLINK = "com.yoinsapp.signin.deder.link";

        public static final String SHIPMENT_LIST = "com.yoinsapp.ShipmentList";
        public static final String DEFAULT_SHIPMENT = "com.yoinsapp.DefaultShipment";
        public static final String PACKAGE_ID = "com.yoinsapp.package_id";
        public static final String SELECTED_SHIPMENT = "com.yoinsapp.SelectedShipment";

        public static final String COUPON_LIST = "com.yoinsapp.CouponList";
        public static final String DEFAULT_COUPON = "com.yoinsapp.DefaultCoupon";
        public static final String SELECTED_COUPON = "com.yoinsapp.SelectedCoupon";

        public static final String DISCOVER_SELF = "com.yoinsapp.DiscoverSelf";

        public static final String SEARCH_KEYWORD = "com.yoinsapp.search.KeyWord";
        public static final String SEARCH_CATEGORY = "com.yoinsapp.search.Category";
        public static final String CATEGORY_ID = "com.yoinsapp.search.CategoryId";
        public static final String CATEGORY_TYPE = "com.yoinsapp.search.CategoryType";
        public static final String CATEGORY_SORT = "com.yoinsapp.search.CategorySort";
        public static final String CATEGORY_NAME = "com.yoinsapp.search.CategoryName";
        public static final String PAGE_SIZE = "com.yoinsapp.search.PageSize";
        public static final String PAGE = "com.yoinsapp.search.Page";
        public static final String FILTER = "com.yoinsapp.search.Filter";
        public static final String IS_FROM_PWA = "com.yoinsapp.search.IsFromPwa";

        public static final String BAG_ITEM = "com.yoinsapp.Bag.Item";
        public static final String CHECKOUT_TOKEN = "com.yoinsapp.Bag.CheckoutToken";
        public static final String PHONE_NUMBER = "com.yoinsapp.Bag.PhoneNumber";

        public static final String ORDER_STATE = "com.yoinsapp.Order.Status";
        public static final String ORDER_ID = "com.yoinsapp.Order.Id";
        public static final String ORDER_TOTAL = "com.yoinsapp.Order.Total";
        public static final String ORDER_LIST = "com.yoinsapp.Order.List";
        public static final String ORDER_TRACK_NUMBER = "com.yoinsapp.Order.TrackNumber";

        public static final String PAYPAL_TOKEN = "com.yoinsapp.Bag.Paypal.token";
        public static final String PAYPAL_CHECK_OUT_RETURN_URL = "com.yoinsapp.Bag.paypal_check_out_return_url";
        public static final String PAYPAL_URL = "com.yoinsapp.Bag.Paypal.Url";
        public static final String PAYPAL_CHECKOUT_GRAND_TOTAL = "com.yoinsapp.Bag.Paypal.CheckoutGrandTotal";
        public static final String PAYPAL_GRAND_TOTAL = "com.yoinsapp.Bag.Paypal.GrandTotal";
        public static final String PAYPAL_CHECKOUT_SHIPPING = "com.yoinsapp.Bag.Paypal.CheckoutShipping";
        public static final String PAYPAL_SHIPPING = "com.yoinsapp.Bag.Paypal.Shipping";
        public static final String PAYPAL_CURRENCY = "com.yoinsapp.Bag.Paypal.Currency";
        public static final String PAYPAL_BAG_ITEMS = "com.yoinsapp.Bag.Paypal.BagItems";
        public static final String PAYPAL_URL_COMMINT = "com.yoinsapp.Bag.Paypal.UrlCommint";
        public static final String PAY_PENDING = "com.yoinsapp.pay.PaymentPending";
        public static final String PAYPAL_PAYMENT_SUCCESS = "com.yoinsapp.Bag.Paypal.PaymentSuccess";
        public static final String PAYPAL_BAG_NUM = "com.yoinsapp.Bag.Paypal.BagNum";

        public static final String CREDIT_CARD_FORM = "com.yoinsapp.Bag.CreditCards.Form";
        public static final String CREDIT_CARD_URL = "com.yoinsapp.Bag.CreditCards.URL";
        public static final String CHECKOUT_OCEAN_PAYMENT_RETURN = "com.yoinsapp.Bag.CreditCards.CheckoutPaymentReturn";

        public static final String POINT_TYPE = "com.yoinsapp.PointType";

        public static final String IMAGES = "com.yoinsapp.Images";
        public static final String IMAGE_URL = "com.yoinsapp.Images";
        public static final String JSON = "com.yoinsapp.json";
        public static final String IMAGE_INDEX = "com.yoinsapp.ImageIndex";

        public static final String USER_LOGOUT = "com.yoinsapp.user.logout";

        public static final String WEB_TITLE = "com.yoinsapp.WebTitle";
        public static final String WEB_URL = "com.yoinsapp.WebUrl";
        public static final String WEB_AGENT = "com.yoinsapp.Web.agent";
        public static final String TITLE = "com.yoinsapp.title";
        public static final String ATTR_COLOR = "com.yoinsapp.attr_color";
        public static final String CATEGORY_WEBPAGE_URL = "com.yoinsapp.webpageUrl";
        public static final String CREDIT_CARD_IDEAL = "CREDIT_CARD_IDEAL";
        public static final String WEB_PURE_LOAD = "web_pure_load";
    }

    public interface ExtraValue {
        int BASE = 2333999;
        int PUSH = BASE + 1;
    }

    public static class RequestCodes {
        public static final int COUNTRY_CODE = 1;
        public static final int ZONE_CODE = 2;
        public static final int ADDRESS_CODE = 3;
        public static final int PROFILE_CODE = 4;
        public static final int SELECT_PICTURE = 5; // 从图库中选择图片
        public static final int SELECT_CAMER = 6; // 用相机拍摄照片
        public static final int CHANGE_SHIPMENT = 7;
        public static final int CHANGE_COUPON = 8;
        public static final int BAG_CODE = 9;
        public static final int CHECK_OUT_CODE = 11;
        public static final int PRODUCT_DETAIL_CODE = 12;
        public static final int PURCHASE_CODE = 13;
        public static final int WISH_CODE = 14;
        public static final int CREDIT_CARD_CODE = 15;
        public static final int SEARCH = 10;
        public static final int BAG_ITEM_CODE = 19;
        public static final int SETTING = 20;
        public static final int LOGIN = 101;
        public static final int LOGOUT = 102;
        public static final int REVIEW = 103;
    }

    public static class ResponseCode {
        public static final String CODE = "code";
        public static final String RESULT = "result";
    }

    // 推荐位
    public interface RecPage {
        String MAIN_ACTIVITY = "MainFragmentActivity";
        String ORDER_DETAIL_ACTIVITY = "OrderDetailActivity";
        String PREORDER_LIST_ACTIVITY = "PreorderListActivity";
        String CATEGORY_FILTER_ACTIVITY = "CategoryFilterActivity";
        String SEARCH_RESULT_ACTIVITY = "SearchResultActivity";
        String CHECK_OUT_ACTIVITY = "CheckOutActivity";
        String SHOP_CART_ACTIVITY = "ShopCartActivity";
        String PRODUCT_HISTORY_ACTIVITY = "ProductHistoryActivity";
        String PRODUCT_INFO_ACTIVITY = "ProductInfoActivity";
        String PRODUCT_REVIEW_DETAIL_ACTIVITY = "ProductReviewDetailActivity";
        String WISH_LIST_ACTIVITY = "WishListActivity";
    }

    // 对应的服务器环境
    public static final int HOST_PRODUCT = 1;
    public static final int HOST_CUSTIOM = 8;
    public static final String HOSTKEY = ".yoins.com";

    public static final int LOGIN_TYPE_NONE = 0;
}
