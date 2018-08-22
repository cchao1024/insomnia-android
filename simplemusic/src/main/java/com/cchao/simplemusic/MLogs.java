package com.cchao.simplemusic;

import android.util.Log;

/**
 * @author cchao
 * @version 8/21/18.
 */
public class MLogs {

    public static final void d(String tag, String content) {
        Log.d("simpleMusic" + tag, content);
    }

    public static final void d(String content) {
        Log.d("", content);
    }

    public static final void e(Throwable content) {
        content.printStackTrace();
    }

}
