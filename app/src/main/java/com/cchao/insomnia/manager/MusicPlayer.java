package com.cchao.insomnia.manager;

import android.media.MediaPlayer;

import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.model.javabean.fall.FallMusic;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;

import java.io.IOException;

import io.reactivex.schedulers.Schedulers;

/**
 * @author cchao
 * @version 8/14/18.
 */
public class MusicPlayer {

    private static MediaPlayer mMediaPlayer = new MediaPlayer();
    private static FallMusic mCurMusic;

    public interface State {
        String Prepare = "prepare";
        String Playing = "Playing";
        String Pause = "Pause";
        String Init = "Init";
    }

    public static String getCurPlayingId() {
        if (mCurMusic != null) {
            return mCurMusic.getId();
        }
        return "";
    }

    public static void init() {
        mMediaPlayer.setOnErrorListener((mediaPlayer, i, i1) -> {
            UiHelper.showToast("播放出现异常");
            RxBus.get().postEvent(Constants.Event.Update_Play_Status, State.Init);
            return false;
        });

        mMediaPlayer.setOnPreparedListener(mediaPlayer -> {
            // 请求+播放计数
            RetrofitHelper.getApis().play(mCurMusic.getId())
                .subscribeOn(Schedulers.io())
                .subscribe(RxHelper.getNothingObserver());
            mMediaPlayer.start();
            RxBus.get().postEvent(Constants.Event.Update_Play_Status, State.Playing);
        });
    }

    public static boolean isPlaying() {
        return mMediaPlayer.isPlaying();
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

    public static void playNow(FallMusic item) {
        mCurMusic = item;
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
            Logs.logEvent("playNow " + item.getSrc());
            mMediaPlayer.setDataSource(item.getSrc());
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
            RxBus.get().postEvent(Constants.Event.Update_Play_Status, State.Prepare);
        } catch (IOException e) {
            Logs.logException(e);
        }
    }
}
