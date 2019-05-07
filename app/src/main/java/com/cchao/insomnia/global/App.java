package com.cchao.insomnia.global;

import android.app.Application;
import android.content.Context;

import com.cchao.insomnia.api.HttpClientManager;
import com.cchao.insomnia.manager.MusicPlayer;
import com.cchao.simplelib.LibCore;

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
        MusicPlayer.init();
    }

    private void initSimpleLib() {
        LibCore.init(this, new LibCore.InfoSupport() {
            @Override
            public OkHttpClient getOkHttpClient() {
                return HttpClientManager.getWrapClient();
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
