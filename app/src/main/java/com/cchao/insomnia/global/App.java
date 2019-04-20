package com.cchao.insomnia.global;

import android.app.Application;
import android.content.Context;

import com.cchao.insomnia.api.HttpClientManager;
import com.cchao.insomnia.manager.MusicHelper;
import com.cchao.simplelib.Const;
import com.cchao.simplelib.LibCore;
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
                .setUseMediaPlayer(false)
                .setAutoPlayNext(true)
                .build();
            musicLibrary.init();
            MusicHelper.init();
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

            @Override
            public LibCore.LibConfig getLibConfig() {
                return new LibCore.LibConfig()
                    .setTitleBarStyle(Const.TitleStyle.title)
                    .setOverrideCookieJar(false);
            }
        });
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }
}
