package com.cchao.sleeping.manager;

import com.cchao.simplelib.core.GsonUtil;
import com.cchao.simplelib.core.PrefHelper;
import com.cchao.simplelib.util.StringHelper;
import com.cchao.sleeping.global.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cchao
 * @version 9/9/18.
 */
public class UserManager {

    private static List<String> mWishList = new ArrayList<>();

    static {
        mWishList = GsonUtil.jsonToList(PrefHelper.getString(Constants.Prefs.WISH_LIST, "[]"), String.class);
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

}
