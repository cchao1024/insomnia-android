package com.cchao.sleeping.ui.music;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.cchao.simplelib.core.CollectionHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.sleeping.R;
import com.cchao.sleeping.databinding.MusicPlayActivityBinding;
import com.cchao.sleeping.manager.MusicHelper;
import com.lzx.musiclibrary.aidl.listener.OnPlayerEventListener;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.constans.PlayMode;
import com.lzx.musiclibrary.constans.State;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * @author cchao
 * @version 8/13/18.
 */
public class MusicPlayerActivity extends BaseToolbarActivity<MusicPlayActivityBinding> implements View.OnClickListener {

    PLayListFragment mPLayListFragment;

    @Override
    protected int getLayout() {
        return R.layout.music_play_activity;
    }

    @Override
    protected void initEventAndData() {
        mDataBind.setClicker(this);
        if (MusicManager.get().getStatus() == State.STATE_PLAYING) {
            mDataBind.setSong(MusicManager.get().getCurrPlayingMusic());
        } else if (CollectionHelper.isNotEmptyList(MusicManager.get().getPlayList())) {
            mDataBind.setSong(MusicManager.get().getPlayList().get(0));
        }
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
                mDataBind.playPause.setImageResource(R.drawable.music_play);
            }

            @Override
            public void onPlayerStop() {
                mDataBind.playPause.setImageResource(R.drawable.music_play);
            }

            @Override
            public void onError(String errorMsg) {
                UiHelper.showToast(errorMsg);
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
                break;
            case R.id.play_mode_switch:
                AppCompatImageView modeSwitch = (AppCompatImageView) v;
                MusicHelper.changePlayMode();
                switch (MusicManager.get().getPlayMode()) {
                    case PlayMode.PLAY_IN_SINGLE_LOOP:
                        modeSwitch.setImageResource(R.drawable.ic_play_mode_single);
                        showText("单曲循环");
                        break;
                    case PlayMode.PLAY_IN_RANDOM:
                        modeSwitch.setImageResource(R.drawable.ic_play_mode_shuffle);
                        showText("随机播放");
                        break;
                    case PlayMode.PLAY_IN_LIST_LOOP:
                        modeSwitch.setImageResource(R.drawable.ic_play_mode_loop);
                        showText("列表循环");
                        break;
                }
                break;
            case R.id.wish:
                showText("wish");
                break;
            case R.id.play_list:
                showText("play_list");
                if (mPLayListFragment == null) {
                    mPLayListFragment = new PLayListFragment();
                }
                mPLayListFragment.show(getSupportFragmentManager(), "PLayListFragment");
                break;
        }
    }
}