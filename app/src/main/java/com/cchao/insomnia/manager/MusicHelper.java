package com.cchao.insomnia.manager;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.cchao.simplelib.core.PrefHelper;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.model.javabean.fall.FallMusic;
import com.lzx.musiclibrary.aidl.listener.OnPlayerEventListener;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.constans.PlayMode;
import com.lzx.musiclibrary.manager.MusicManager;

import org.apache.commons.lang3.StringUtils;

import java.util.Observable;
import java.util.Observer;

import io.reactivex.schedulers.Schedulers;

/**
 * @author cchao
 * @version 8/14/18.
 */
public class MusicHelper {

    // 选择的播放方式
    private static int selectPlayMode = 2;

    public static final Pair<Integer, String>[] Play_Mode = new Pair[]{
        Pair.create(PlayMode.PLAY_IN_SINGLE_LOOP, "单曲循环"),
        Pair.create(PlayMode.PLAY_IN_RANDOM, "随机播放"),
        Pair.create(PlayMode.PLAY_IN_LIST_LOOP, "列表循环"),
    };

    public static String getCurPlayingId() {
        if (MusicManager.get().getCurrPlayingMusic() != null
            && StringHelper.isNotEmpty(MusicManager.get().getCurrPlayingMusic().getSongId())) {
            return MusicManager.get().getCurrPlayingMusic().getSongId();
        } else {
            return "null";
        }
    }

    public static void changePlayMode() {
        int toPlayMode = (++selectPlayMode) % Play_Mode.length;
        PrefHelper.putInt(Constants.Prefs.MUSIC_PLAY_MODE, toPlayMode);
        MusicManager.get().setPlayMode(Play_Mode[toPlayMode].first);
    }

    public static void init() {
        selectPlayMode = PrefHelper.getInt(Constants.Prefs.MUSIC_PLAY_MODE, 2);
        MusicManager.get().setPlayMode(Play_Mode[selectPlayMode].first);
        MusicManager.get().addStateObservable(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                RxBus.getDefault().postEvent(Constants.Event.Update_Play_Status);
            }
        });

        MusicManager.get().addPlayerEventListener(new OnPlayerEventListener() {
            @Override
            public void onMusicSwitch(SongInfo music) {
                RxBus.getDefault().postEvent(Constants.Event.Update_Play_Status);
            }

            @Override
            public void onPlayerStart() {
                String id = MusicManager.get().getCurrPlayingMusic().getSongId();
                RetrofitHelper.getApis().playCount(id)
                    .subscribeOn(Schedulers.io())
                    .subscribe(RxHelper.getNothingObserver());

                RxBus.getDefault().postEvent(Constants.Event.Update_Play_Status);
            }

            @Override
            public void onPlayerPause() {
                RxBus.getDefault().postEvent(Constants.Event.Update_Play_Status);
            }

            @Override
            public void onPlayCompletion() {
                RxBus.getDefault().postEvent(Constants.Event.Update_Play_Status);
            }

            @Override
            public void onPlayerStop() {
                RxBus.getDefault().postEvent(Constants.Event.Update_Play_Status);
            }

            @Override
            public void onError(String errorMsg) {
                RxBus.getDefault().postEvent(Constants.Event.Update_Play_Status);
                UiHelper.showToast(errorMsg);
            }

            @Override
            public void onAsyncLoading(boolean isFinishLoading) {

            }
        });
    }

    public static void playNow(FallMusic item) {
        SongInfo songInfo = getSongInfo(item);
        MusicManager.get().playMusicByInfo(songInfo);
    }


    public static void addToPlayList(FallMusic item) {
        SongInfo songInfo = getSongInfo(item);
        MusicManager.get().getPlayList().add(songInfo);
    }

    /**
     * adapter
     */
    @NonNull
    private static SongInfo getSongInfo(FallMusic item) {
        SongInfo songInfo = new SongInfo();

        songInfo.setSongId(String.valueOf(item.getId()));
        songInfo.setSongUrl(item.getSrc());
        songInfo.setSongName(item.getName());
        songInfo.setArtist(item.getSinger());

        return songInfo;
    }
}
