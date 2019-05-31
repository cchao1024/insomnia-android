package com.cchao.insomnia.manager;

import android.media.MediaPlayer;

import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.model.javabean.fall.FallMusic;
import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.danikula.videocache.HttpProxyCacheServer;

import java.io.IOException;

import io.reactivex.schedulers.Schedulers;

/**
 * @author cchao
 * @version 8/14/18.
 */
public class MusicPlayer {

    private static HttpProxyCacheServer proxy;
    private static MediaPlayer mMediaPlayer = new MediaPlayer();
    private static FallMusic mCurMusic;

    public interface State {
        String Prepare = "prepare";
        String Playing = "Playing";
        String Pause = "Pause";
        String Init = "Init";
    }

    public static MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    public static long getCurPlayingId() {
        if (mCurMusic != null) {
            return mCurMusic.getId();
        }
        return 0;
    }

    public static void init() {
        mMediaPlayer.setOnErrorListener((mediaPlayer, i, i1) -> {
            UiHelper.showToast("播放出现异常");
            RxBus.get().postEvent(Constants.Event.Update_Play_Status, State.Init);
            return false;
        });

        mMediaPlayer.setOnPreparedListener(mediaPlayer -> {
            // 请求+播放计数
            RetrofitHelper.getApis().play(String.valueOf(mCurMusic.getId()))
                .subscribeOn(Schedulers.io())
                .subscribe(RxHelper.getNothingObserver());
            mMediaPlayer.start();
            RxBus.get().postEvent(Constants.Event.Update_Play_Status, State.Playing);
            UiHelper.showToast("正在播放 " + mCurMusic.getName());
        });
    }

    public static boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public static boolean isCurPlaying(FallMusic fallMusic) {
        return fallMusic.getId() == getCurPlayingId();
    }

    public static void clickDisk() {
        if (mMediaPlayer.isPlaying()) {
            pause();
        } else {
            if (mCurMusic != null) {
                mMediaPlayer.start();
            }
        }
    }

    public static void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            RxBus.get().postEvent(Constants.Event.Update_Play_Status, State.Pause);
        }
    }

    public static void playOrPause(FallMusic item) {
        if (getCurPlayingId() == item.getId()) {
            pause();
        } else {
            playNow(item);
        }
    }

    public static void playNow(FallMusic item) {
        // 相同不处理
        if (mCurMusic != null && mCurMusic.getSrc().equals(item.getSrc())) {
            return;
        }
        mCurMusic = item;
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
            }
            Logs.logEvent("playNow " + item.getSrc());
            mMediaPlayer.setDataSource(getPlaySrc(item));
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
            RxBus.get().postEvent(Constants.Event.Update_Play_Status, State.Prepare);
        } catch (IOException e) {
            Logs.logException(e);
        }
    }

    private static String getPlaySrc(FallMusic fallMusic) {
        return getProxy().getProxyUrl(fallMusic.getSrc());
    }

    /**
     * 缓存代理
     */
    public static HttpProxyCacheServer getProxy() {
        if (proxy == null) {
            proxy = new HttpProxyCacheServer.Builder(LibCore.getContext())
                // 1 Gb for cache
                .maxCacheSize(1024 * 1024 * 1024)
                .maxCacheFilesCount(40)
                .build();
        }
        return proxy;
    }
}
