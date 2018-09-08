package com.cchao.sleeping.manager;

import android.support.annotation.NonNull;

import com.cchao.simplelib.core.PrefHelper;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.util.Tuple;
import com.cchao.sleeping.global.Constants;
import com.cchao.sleeping.model.javabean.fall.FallMusic;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.constans.PlayMode;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.Observable;
import java.util.Observer;

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
        int toPlayMode = (selectPlayMode + 1) % Play_Mode.length;
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
        return songInfo;
    }
}
