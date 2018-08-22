package com.cchao.simplemusic.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;

import com.cchao.simplemusic.IMusicPlayer;
import com.cchao.simplemusic.IMusicPlayerListener;
import com.cchao.simplemusic.IPlayStateListener;
import com.cchao.simplemusic.MConstans;
import com.cchao.simplemusic.MLogs;
import com.cchao.simplemusic.bean.MusicServiceBean;
import com.cchao.simplemusic.helper.GsonHelper;
import com.cchao.simplemusic.model.MusicItem;
import com.cchao.simplemusic.net.log.AGLog;
import com.cchao.simplemusic.util.ToastUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * author：agxxxx on 2017/3/3 10:49
 * email：agxxxx@126.com
 * blog: http://www.jianshu.com/u/c1a3c4c943e5
 * github: https://github.com/agxxxx
 * Created by Administrator on 2017/3/3.
 */
public class MusicService extends Service {

    public static int MUSIC_CURRENT_MODE = MUSIC_PLAY_MODE_RANDOM;


    private MediaPlayer mMediaPlayer;
    int mPlayerState = MConstans.State.Stop;
    int mPlayMode = MConstans.PlayMode.Random;
    private int mCurPlayIndex = 0;
    private Timer mTimer;

    RemoteCallbackList<IPlayStateListener> mListenerList = new RemoteCallbackList<>();

    ArrayList<MusicItem> mMusicQueue = new ArrayList<>();
    Binder mBinder = new ServiceStub(this);

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        initMediaPlayer();
        initAudioFocus();
        super.onCreate();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        // Error回调
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int i1) {
                broadError("onError " + what);
                return false;
            }
        });
        // Prepared回调
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mPlayerState = MConstans.State.Prepared;
                play();
            }
        });
        // 播放一首完成回调
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next();
            }
        });
    }

    private void initAudioFocus() {
        AudioManager audioManager = ((AudioManager) getSystemService(Context.AUDIO_SERVICE));
        if (audioManager == null) {
            return;
        }
        // STREAM_MUSIC
        audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        // resume playback
                        mMediaPlayer.start();
                        mMediaPlayer.setVolume(1.0f, 1.0f);
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        // Lost focus for an unbounded amount of time: stopSong playback and release media player
                        if (mMediaPlayer.isPlaying())
                            mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        // Lost focus for a short time, but we have to stopSong
                        // playback. We don't release the media player because playback
                        // is likely to resume
                        if (mMediaPlayer.isPlaying())
                            mMediaPlayer.pause();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        // Lost focus for a short time, but it's ok to keep playing
                        // at an attenuated level
                        if (mMediaPlayer.isPlaying())
                            mMediaPlayer.setVolume(0.1f, 0.1f);
                        break;
                }

            }
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    //region 操作播放行为
    void stop() throws RemoteException {
        if (mMediaPlayer == null) {
            broadError("mMediaPlayer == null");
            return;
        }
        mMediaPlayer.stop();
        mPlayerState = MConstans.State.Stop;
    }

    void play() {
        switch (mPlayerState) {
            // 从停止中开始播放，reset后播放上一次播放队列索引
            case MConstans.State.Stop:
                try {
                    MusicItem musicItem = mMusicQueue.get(mCurPlayIndex);
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(musicItem.getSrc());
                    mMediaPlayer.prepare();
                    mPlayerState = MConstans.State.Loading;
                } catch (IOException ex) {
                    MLogs.e(ex);
                }
                break;
            // 暂停  准备完成
            case MConstans.State.Pause:
            case MConstans.State.Prepared:
                try {
                    mMediaPlayer.start();
                } catch (Exception ex) {
                    MLogs.e(ex);
                }
                break;
        }
    }

    void pause(){

    }
    void prev() {
        if (mCurPlayIndex > 0) {
            mCurPlayIndex--;
        } else {
            mCurPlayIndex = mMusicQueue.size() - 1;
        }
        play();
    }

    void next() {
        switch (MUSIC_CURRENT_MODE) {
            case MConstans.PlayMode.Random:
                if (mMusicQueue != null && mMusicQueue.size() > 0) {
                    Random random = new Random();
                    mCurPlayIndex = random.nextInt(mMusicQueue.size());
                    play();
                }
                break;
            case MConstans.PlayMode.Repeat:
                break;
            case MConstans.PlayMode.Order:
                break;
            case MConstans.PlayMode.Single:

                play();
                break;
        }
        if (++mCurPlayIndex >= mMusicQueue.size()) {
            mCurPlayIndex = 0;
        }
        play();
    }

    private long seek(long pos) throws RemoteException {
        return 0;
    }
    //endregion

    public void enqueue(MusicItem item) throws RemoteException {

    }

    //region 获取、设置状态
    public void setPlayMode(int mode) throws RemoteException {

    }

    public int getPlayMode() throws RemoteException {
        return 0;
    }

    public int getQueueSize() throws RemoteException {
        return 0;
    }

    public int getShuffleMode() throws RemoteException {
        return 0;
    }

    public int getRepeatMode() throws RemoteException {
        return 0;
    }
    //endregion

    //region 广播回调 状态
    protected void broadError(String error) {
        try {
            final int N = mListenerList.beginBroadcast();
            for (int i = 0; i < N; i++) {
                IPlayStateListener broadcastItem = mListenerList.getBroadcastItem(i);
                if (broadcastItem != null) {
                    broadcastItem.onError(error);
                }
            }
        } catch (RemoteException e) {
            MLogs.e(e);
        } finally {
            try {
                mListenerList.finishBroadcast();
            } catch (IllegalArgumentException illegalArgumentException) {
                MLogs.e(illegalArgumentException);
            }
        }
    }

    protected void broadState(int state, String extra) {
        try {
            final int N = mListenerList.beginBroadcast();
            for (int i = 0; i < N; i++) {
                IPlayStateListener broadcastItem = mListenerList.getBroadcastItem(i);
                if (broadcastItem != null) {
                    broadcastItem.onStateUpdate(state, extra);
                }
            }
        } catch (RemoteException e) {
            MLogs.e(e);
        } finally {
            try {
                mListenerList.finishBroadcast();
            } catch (IllegalArgumentException illegalArgumentException) {
                MLogs.e(illegalArgumentException);
            }
        }
    }
    //endregion


    final class ServiceStub extends IMusicPlayer.Stub {

        private final WeakReference<MusicService> mService;

        ServiceStub(final MusicService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return false;
        }

        @Override
        public void stop() throws RemoteException {
            mService.get().stop();
        }

        @Override
        public void pause() throws RemoteException {
            mService.get().pause();
        }


        @Override
        public void play() throws RemoteException {
            mService.get().play();
        }

        @Override
        public void prev() throws RemoteException {
            mService.get().prev();
        }

        @Override
        public void next() throws RemoteException {
            mService.get().gotoNext(true);
        }

        @Override
        public void enqueue(MusicItem item) throws RemoteException {

        }

        @Override
        public void setPlayMode(int mode) throws RemoteException {

        }

        @Override
        public int getPlayMode() throws RemoteException {
            return 0;
        }

        @Override
        public int getQueueSize() throws RemoteException {
            return 0;
        }

        @Override
        public long seek(long pos) throws RemoteException {
            return 0;
        }

        @Override
        public int getShuffleMode() throws RemoteException {
            return 0;
        }

        @Override
        public int getRepeatMode() throws RemoteException {
            return 0;
        }

        @Override
        public void registerListener(IPlayStateListener listener) throws RemoteException {
            mService.get().mListenerList.register(listener);
        }

        @Override
        public void unregisterListener(IPlayStateListener listener) throws RemoteException {
            mService.get().mListenerList.unregister(listener);

        }
    }
}
