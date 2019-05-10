package com.cchao.insomnia.manager;

import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.model.javabean.user.UserBean;
import com.cchao.simplelib.core.GsonUtil;
import com.cchao.simplelib.core.PrefHelper;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.util.StringHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * @author cchao
 * @version 9/9/18.
 */
public class UserManager {

    private static List<String> mWishList = new ArrayList<>();
    private static UserBean mUserBean;

    static {
        mWishList = GsonUtil.jsonToList(PrefHelper.getString(Constants.Prefs.WISH_LIST, "[]"), String.class);
        mUserBean = GsonUtil.fromJson(PrefHelper.getString(Constants.Prefs.USER_INFO), UserBean.class);
    }

    public static boolean isInWishList(String id) {
        if (StringHelper.isEmpty(id)) {
            return false;
        }
        for (String s : mWishList) {
            if (id.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public static void toggleWish(String id) {
        mWishList.add(id);
        PrefHelper.putString(Constants.Prefs.WISH_LIST, GsonUtil.toJson(mWishList));
    }

    public static void setUserBean(UserBean userBean) {
        mUserBean = userBean;
        PrefHelper.putString(Constants.Prefs.USER_INFO, GsonUtil.toJson(userBean));
        RxBus.get().post(userBean);
    }

    public static UserBean getUserBean() {
        return mUserBean;
    }

    public static String getToken() {
        if (mUserBean == null) {
            return "";
        }
        return mUserBean.getToken();
    }

    public static boolean isVisitor() {
        return true;
    }

    public static void giveLike(String type, long id) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("id", String.valueOf(id));
        Disposable disposable = RetrofitHelper.getApis().giveLike(map)
            .compose(RxHelper.toMain())
            .subscribe(respBean -> {
                if (respBean.isCodeFail()) {
                    UiHelper.showToast(respBean.getMsg());
                    return;
                }
            }, RxHelper.getErrorConsumer());
    }
}
