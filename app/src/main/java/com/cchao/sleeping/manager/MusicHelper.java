package com.cchao.sleeping.manager;

import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cchao
 * @version 8/14/18.
 */
public class MusicHelper {

    public static void addSongToPlayList(SongInfo songInfo) {
        if (MusicManager.get().getPlayList() == null) {
            List<SongInfo> list = new ArrayList<>();
            list.add(songInfo);
            MusicManager.get().setPlayList(new ArrayList<>());
            return;
        }
        MusicManager.get().getPlayList().add(songInfo);
    }

}
