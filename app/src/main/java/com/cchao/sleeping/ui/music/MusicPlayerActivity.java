package com.cchao.sleeping.ui.music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplemusic.IMusicPlayer;
import com.cchao.simplemusic.MusicService;
import com.cchao.sleeping.R;
import com.cchao.sleeping.databinding.MusicActivityBinding;

/**
 * @author cchao
 * @version 8/13/18.
 */
public class MusicPlayerActivity extends BaseToolbarActivity<MusicActivityBinding> implements View.OnClickListener {

    ServiceConnection mServiceConnection;
    IMusicPlayer mMusicPlayer;

    @Override
    protected int getLayout() {
        return R.layout.music_activity;
    }

    @Override
    protected void initEventAndData() {
        mDataBind.setClicker(this);
        initPlayService();
    }

    private void initPlayService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMusicPlayer = (IMusicPlayer) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onLoadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    @Override
    public void onClick(View v) {
        try {
            if (mMusicPlayer == null) {
                Logs.d("MusicPlayer is null");
                return;
            }
            switch (v.getId()) {
                case R.id.play_next:
                    mMusicPlayer.next();
                    break;
                case R.id.play_pause:
                    mMusicPlayer.pause();
                    break;
                case R.id.play_pre:
                    mMusicPlayer.prev();
                    break;
            }
        } catch (RemoteException e) {
            ExceptionCollect.logException(e);
        }
    }
}
