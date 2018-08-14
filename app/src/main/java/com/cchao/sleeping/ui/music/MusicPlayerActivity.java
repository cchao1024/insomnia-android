package com.cchao.sleeping.ui.music;

import android.view.View;

import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.sleeping.R;
import com.cchao.sleeping.databinding.MusicActivityBinding;
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
    }

    @Override
    protected void onLoadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_next:
                MusicManager.get().playNext();
                break;
            case R.id.play_pause:
                if (MusicManager.get().getStatus() == State.STATE_PAUSED) {
                    MusicManager.get().resumeMusic();
                } else {
                    MusicManager.get().pauseMusic();
                }
                break;
            case R.id.play_pre:
                MusicManager.get().playPre();
                break;
        }
    }
}
