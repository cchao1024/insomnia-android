package com.cchao.sleeping.manager;

import android.support.annotation.NonNull;

import com.cchao.simplelib.core.PrefHelper;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.util.Tuple;
import com.cchao.sleeping.api.RetrofitHelper;
import com.cchao.sleeping.global.Constants;
import com.cchao.sleeping.model.javabean.fall.FallMusic;
import com.lzx.musiclibrary.aidl.listener.OnPlayerEventListener;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.constans.PlayMode;
import com.lzx.musiclibrary.manager.MusicManager;

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

    public static final Tuple.Tuple2<Integer, String>[] Play_Mode = new Tuple.Tuple2[]{
        Tuple.tuple(PlayMode.PLAY_IN_SINGLE_LOOP, "单曲循环"),
        Tuple.tuple(PlayMode.PLAY_IN_RANDOM, "随机播放"),
        Tuple.tuple(PlayMode.PLAY_IN_LIST_LOOP, "列表循环"),
    };

    public static void changePlayMode() {
        int toPlayMode = (++selectPlayMode) % Play_Mode.length;
        PrefHelper.putInt(Constants.Prefs.MUSIC_PLAY_MODE, toPlayMode);
        MusicManager.get().setPlayMode(Play_Mode[toPlayMode].a);
    }

    public static void init() {
        selectPlayMode = PrefHelper.getInt(Constants.Prefs.MUSIC_PLAY_MODE, 2);
        MusicManager.get().setPlayMode(Play_Mode[selectPlayMode].a);
        MusicManager.get().addStateObservable(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                int msg = (int) arg;
                RxBus.getDefault().postEvent(msg);
            }
        });

        MusicManager.get().addPlayerEventListener(new OnPlayerEventListener() {
            @Override
            public void onMusicSwitch(SongInfo music) {

            }

            @Override
            public void onPlayerStart() {
                String id = MusicManager.get().getCurrPlayingMusic().getSongId();
                RetrofitHelper.getApis().playCount(id)
                    .subscribeOn(Schedulers.io())
                    .subscribe(RxHelper.getNothingObserver());
            }

            @Override
            public void onPlayerPause() {

            }

            @Override
            public void onPlayCompletion() {

            }

            @Override
            public void onPlayerStop() {

            }

            @Override
            public void onError(String errorMsg) {

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
