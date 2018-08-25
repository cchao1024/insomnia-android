package com.cchao.simplemusic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.cchao.simplemusic.model.MusicItem;

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

    MediaPlayer mMediaPlayer;
    int mPlayerState = MConstans.State.Stop;
    int mPlayMode = MConstans.PlayMode.Random;
    private int mCurPlayIndex = 0;
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask;

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
                updatePlayState(MConstans.State.Prepared);
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
        // seek
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                updatePlayState(MConstans.State.Playing);
            }
        });
    }

    // 更新播放状态
    void updatePlayState(int playerState) {
        MLogs.d("updatePlayState " + playerState);
        if (playerState == mPlayerState && mPlayerState != MConstans.State.Playing) {
            MLogs.d("重复的状态 " + playerState);
            return;
        }
        if (playerState == MConstans.State.Pause || playerState == MConstans.State.Stop) {
            mTimerTask.cancel();
        }
        mPlayerState = playerState;
        broadState(mPlayerState, "");
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
    void stop() {
        if (checkError()) {
            return;
        }
        mMediaPlayer.stop();
        updatePlayState(MConstans.State.Stop);
    }

    private boolean checkError() {
        if (mMediaPlayer == null) {
            broadError("MediaPlayer == null");
            return true;
        }
        return false;
    }

    void play() {
        if (checkError()) {
            return;
        }
        switch (mPlayerState) {
            // 从停止中开始播放，reset后播放上一次播放队列索引
            case MConstans.State.Stop:
                try {
                    MusicItem musicItem = mMusicQueue.get(mCurPlayIndex);
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(musicItem.getSrc());
                    mMediaPlayer.prepare();
                    updatePlayState(MConstans.State.Loading);
                } catch (IOException ex) {
                    MLogs.e(ex);
                }
                break;
            // 暂停  准备完成
            case MConstans.State.Pause:
            case MConstans.State.Prepared:
                try {
                    mMediaPlayer.start();
                    mPlayerState = MConstans.State.Playing;
                    mTimer.schedule(mTimerTask = new TimerTask() {
                        @Override
                        public void run() {
                            broadState(mPlayerState
                                , mMediaPlayer.getCurrentPosition() + "");
                        }
                    }, 0, 1000);
                } catch (Exception ex) {
                    MLogs.e(ex);
                }
                break;
        }
    }

    void pause() {
        if (checkError()) {
            return;
        }
        mMediaPlayer.pause();
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
        switch (mPlayMode) {
            case MConstans.PlayMode.Random:
                if (mMusicQueue.size() > 0) {
                    Random random = new Random();
                    mCurPlayIndex = random.nextInt(mMusicQueue.size());
                    play();
                }
                break;
            case MConstans.PlayMode.Repeat:
//                play();
                break;
            case MConstans.PlayMode.Order:
                mCurPlayIndex = (mCurPlayIndex + 1) % mMusicQueue.size();
                play();
                break;
        }
    }

    void seek(int pos){
        if (checkError()) {
            return;
        }
        if (mPlayerState == MConstans.State.Playing
            || mPlayerState == MConstans.State.Loading
            || mPlayerState == MConstans.State.Pause) {

            mMediaPlayer.seekTo(pos);
            updatePlayState(MConstans.State.Loading);
        }
    }
    //endregion

    public void enqueue(MusicItem item){
        for (MusicItem musicItem : mMusicQueue) {
            if (musicItem.getId().equalsIgnoreCase(item.getId())) {
//                mMusicQueue.remove(musicItem);
                return;
            }
        }
        mMusicQueue.add(item);
    }

    //region 获取、设置状态
    public void setPlayMode(int mode){
        if (mode != MConstans.PlayMode.Order
            && mode != MConstans.PlayMode.Repeat
            && mode != MConstans.PlayMode.Random) {

            broadError("Please choose the right model");
            return;
        }
        // 循环播放
        mMediaPlayer.setLooping(mode == MConstans.PlayMode.Repeat);
        mPlayMode = mode;
    }

    public int getPlayMode(){
        return mPlayMode;
    }

    public int getQueueSize(){
        return mMusicQueue.size();
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
        public int getPlayState(){
            return mService.get().mPlayerState;
        }

        @Override
        public void stop(){
            mService.get().stop();
        }

        @Override
        public void pause(){
            mService.get().pause();
        }

        @Override
        public void play(){
            mService.get().play();
        }

        @Override
        public void prev(){
            mService.get().prev();
        }

        @Override
        public void next(){
            mService.get().next();
        }

        @Override
        public void enqueue(MusicItem item){
            mService.get().enqueue(item);
        }

        @Override
        public void setPlayMode(int mode){
            mService.get().setPlayMode(mode);
        }

        @Override
        public int getPlayMode(){
            return mService.get().getPlayMode();
        }

        @Override
        public int getQueueSize(){
            return mService.get().getQueueSize();
        }

        @Override
        public void seek(int pos){
            mService.get().seek(pos);
        }

        @Override
        public void registerListener(IPlayStateListener listener){
            mService.get().mListenerList.register(listener);
        }

        @Override
        public void unregisterListener(IPlayStateListener listener){
            mService.get().mListenerList.unregister(listener);

        }
    }
}
