package com.cchao.insomnia.ui.music;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.cchao.insomnia.R;
import com.cchao.insomnia.databinding.MusicPlayActivityBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.manager.MusicPlayer;
import com.cchao.insomnia.manager.UserManager;
import com.cchao.simplelib.core.CollectionHelper;
import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;
import com.lzx.musiclibrary.aidl.listener.OnPlayerEventListener;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.constans.PlayMode;
import com.lzx.musiclibrary.constans.State;
import com.lzx.musiclibrary.manager.MusicManager;
import com.lzx.musiclibrary.manager.TimerTaskManager;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author cchao
 * @version 8/13/18.
 */
public class MusicPlayerActivity extends BaseTitleBarActivity<MusicPlayActivityBinding> implements View.OnClickListener {

    PLayListFragment mPLayListFragment;
    TimerTaskManager mTaskManager = new TimerTaskManager();
    OnPlayerEventListener mOnPlayerEventListener;
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("mm:ss");

    @Override
    protected int getLayout() {
        return R.layout.music_play_activity;
    }

    @Override
    protected void initEventAndData() {
        mDataBind.setClicker(this);

        initPlayService();
        mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
    }

    private void initPlayService() {
        MusicManager.get().addPlayerEventListener(mOnPlayerEventListener = new OnPlayerEventListener() {
            @Override
            public void onMusicSwitch(SongInfo music) {

            }

            @Override
            public void onPlayerStart() {
                mDataBind.playPause.setImageResource(R.drawable.music_pause);
                updateSongInfo(MusicManager.get().getCurrPlayingMusic());
            }

            @Override
            public void onPlayerPause() {
                mDataBind.playPause.setImageResource(R.drawable.music_play);
                mTaskManager.stopSeekBarUpdate();
            }

            @Override
            public void onPlayCompletion() {
                mDataBind.playPause.setImageResource(R.drawable.music_play);
            }

            @Override
            public void onPlayerStop() {
                mDataBind.playPause.setImageResource(R.drawable.music_play);
                mTaskManager.stopSeekBarUpdate();
            }

            @Override
            public void onError(String errorMsg) {
                UiHelper.showToast(errorMsg);
            }

            @Override
            public void onAsyncLoading(boolean isFinishLoading) {

            }
        });
        mTaskManager.setUpdateProgressTask(new Runnable() {
            @Override
            public void run() {
                int progress = (int) MusicManager.get().getProgress();
                mDataBind.seekBar.setProgress(progress);
                mDataBind.progressTime.setText(mSimpleDateFormat.format(progress));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MusicManager.get().getStatus() == State.STATE_PLAYING) {
            mDataBind.playPause.setImageResource(R.drawable.music_pause);
        }
        updateSongInfo(getCurSong());
    }

    private SongInfo getCurSong() {
        SongInfo songInfo = new SongInfo();
        if (MusicManager.get().getStatus() == State.STATE_PLAYING) {
            songInfo = MusicManager.get().getCurrPlayingMusic();
        } else if (CollectionHelper.isNotEmpty(MusicManager.get().getPlayList())) {
            songInfo = MusicManager.get().getPlayList().get(0);
        }
        return songInfo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTaskManager.onRemoveUpdateProgressTask();
        MusicManager.get().removePlayerEventListener(mOnPlayerEventListener);
    }

    @Override
    protected void onLoadData() {
    }

    protected void updateSongInfo(SongInfo songInfo) {
        songInfo.setDuration(MusicManager.get().getDuration());
        String hhmm = mSimpleDateFormat.format(MusicManager.get().getDuration());

        mDataBind.setSong(songInfo);
        mDataBind.duration.setText(hhmm);

        mDataBind.wish.setImageResource(UserManager.isInWishList(songInfo.getSongId())
            ? R.drawable.ic_wish_ed : R.drawable.ic_wish_no);
        mTaskManager.scheduleSeekBarUpdate();
        ImageLoader.loadImage(mDataBind.musicAlbum, Constants.TEST_IMAGE_PATH);

/*        GlideApp.with(mContext).asDrawable()
            .load(Constants.TEST_IMAGE_PATH)
            .into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    mDataBind.musicAlbum.setImageDrawable(resource);
                }
            });*/
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
//                MusicPlayer.changePlayMode();
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
                String id = getCurSong().getSongId();
                UserManager.toggleWish(id);
                mDataBind.wish.setImageResource(UserManager.isInWishList(id)
                    ? R.drawable.ic_wish_ed : R.drawable.ic_wish_no);
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