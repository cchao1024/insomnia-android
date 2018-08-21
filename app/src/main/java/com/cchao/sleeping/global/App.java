package com.cchao.sleeping.global;

import android.app.Application;
import android.content.Context;

import com.cchao.simplelib.LibCore;
import com.cchao.sleeping.api.HttpClientManager;
import com.lzx.musiclibrary.manager.MusicLibrary;
import com.lzx.musiclibrary.utils.BaseUtil;

import okhttp3.OkHttpClient;

/**
 * @author cchao
 * @version 8/10/18.
 */
public class App extends Application {

    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        App.mInstance = this;
        initSimpleLib();
        initMusic();
    }

    private void initMusic() {
        if (BaseUtil.getCurProcessName(this).equals(getPackageName())) {
            MusicLibrary musicLibrary = new MusicLibrary.Builder(this)
                .setUseMediaPlayer(true)
                .setAutoPlayNext(true)
                .build();
            musicLibrary.init();
        }
    }

    private void initSimpleLib() {
        LibCore.init(this, new LibCore.InfoSupport() {
            @Override
            public OkHttpClient getOkHttpClient() {
                return HttpClientManager.getProdOkHttpClient();
            }

            @Override
            public boolean isDebug() {
                return GLobalInfo.isDebug();
            }

            @Override
            public String getAppName() {
                return App.getContext().getPackageName();
            }
        });
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }
}
