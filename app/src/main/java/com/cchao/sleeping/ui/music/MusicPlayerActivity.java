package com.cchao.sleeping.ui.music;

import android.view.View;

import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.sleeping.R;
import com.cchao.sleeping.databinding.MusicActivityBinding;
import com.lzx.musiclibrary.aidl.listener.OnPlayerEventListener;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.constans.State;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * @author cchao
 * @version 8/13/18.
 */
public class MusicPlayerActivity extends BaseToolbarActivity<MusicActivityBinding> implements View.OnClickListener {

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
        MusicManager.get().addPlayerEventListener(new OnPlayerEventListener() {
            @Override
            public void onMusicSwitch(SongInfo music) {

            }

            @Override
            public void onPlayerStart() {
                mDataBind.playPause.setImageResource(R.drawable.music_pause);
            }

            @Override
            public void onPlayerPause() {
                mDataBind.playPause.setImageResource(R.drawable.music_play);
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

    @Override
    protected void onLoadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_pause:
                if (MusicManager.get().getStatus() == State.STATE_PLAYING) {
                    MusicManager.get().pauseMusic();
                    break;
                }
                if (MusicManager.get().getStatus() == State.STATE_PAUSED) {
                    MusicManager.get().resumeMusic();
                    break;
                }

                break;
            case R.id.play_pre:
                if (MusicManager.get().hasPre()) {
                    MusicManager.get().playPre();
                } else {
                    showText("没有上一首了");
                }
                break;
            case R.id.play_next:
                if (MusicManager.get().hasNext()) {
                    MusicManager.get().playNext();
                } else {
                    showText("没有下一首了");

                }
        }
    }
}