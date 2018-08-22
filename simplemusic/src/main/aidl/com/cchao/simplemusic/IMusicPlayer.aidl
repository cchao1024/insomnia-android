package com.cchao.simplemusic;

import com.cchao.simplemusic.model.MusicItem;
import com.cchao.simplemusic.IPlayStateListener;

interface IMusicPlayer {

        boolean isPlaying();
        void stop();
        void pause();
        void play();
        void prev();
        void next();
        void enqueue(in MusicItem item);
        void setPlayMode(int mode);
        int getPlayMode();
        int getQueueSize();
        long seek(long pos);
        int getShuffleMode();
        int getRepeatMode();

        void registerListener(in IPlayStateListener listener);
        void unregisterListener(in IPlayStateListener listener);
}
