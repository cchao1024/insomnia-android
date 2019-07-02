package com.cchao.insomnia.ui.fall;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cchao.insomnia.BR;
import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.databinding.MusicListBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.manager.MusicPlayer;
import com.cchao.insomnia.model.javabean.RespListBean;
import com.cchao.insomnia.model.javabean.fall.FallMusic;
import com.cchao.insomnia.ui.music.MusicPlayerActivity;
import com.cchao.insomnia.util.AnimHelper;
import com.cchao.insomnia.view.Dialogs;
import com.cchao.insomnia.view.adapter.PageAdapter;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;
import com.lzx.musiclibrary.manager.MusicManager;

import io.reactivex.Observable;

/**
 * @author cchao
 * @version 8/12/18.
 */
public class FallMusicListActivity extends BaseTitleBarActivity<MusicListBinding> {

    RecyclerView mRecycler;
    PageAdapter<FallMusic> mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.music_list;
    }

    @Override
    protected void initEventAndData() {
        setTitleText(getString(R.string.music_list));
        initAdapter();
        initEvent();
        onLoadData();
        mDataBind.setClicker(v -> {
            switch (v.getId()) {
                case R.id.music_disk:
                    Router.turnTo(mContext, MusicPlayerActivity.class).start();
                    break;

            }
        });
    }

    private void initEvent() {
        addSubscribe(RxBus.get().toObservable(event -> {
            switch (event.getCode()) {
                case Constants.Event.Update_Play_Status:
                    updateDiskView();
                    for (FallMusic music : mAdapter.getData()) {
                        music.isPlaying.set(music.getId() == MusicPlayer.getCurPlayingId());
                    }
                    break;
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDiskView();
    }

    private void updateDiskView() {
        if (MusicManager.get() == null) {
            return;
        }
        if (MusicManager.isPlaying()) {
            if (MusicManager.get().getCurrPlayingMusic() == null) {
                return;
            }
            mDataBind.musicDisk.setVisibility(View.VISIBLE);
            AnimHelper.startRotate(mDataBind.musicDisk);
        } else {
            mDataBind.musicDisk.setVisibility(View.GONE);
        }
    }

    private void initAdapter() {
        mRecycler = mDataBind.recyclerView;
        mDataBind.refreshLayout.setEnabled(false);

        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(UiHelper.getDrawable(R.drawable.music_list_divider));
        mRecycler.addItemDecoration(divider);

        mRecycler.setAdapter(mAdapter = new PageAdapter<FallMusic>
            (R.layout.music_item, mDisposable, this) {

            @Override
            protected Observable<RespListBean<FallMusic>> getLoadObservable(int page) {
                return RetrofitHelper.getApis().getMusicList(page);
            }

            @Override
            protected void convert(DataBindViewHolder helper, FallMusic item) {
                helper.getBinding().setVariable(BR.item, item);
                helper.setText(R.id.order_num, helper.getLayoutPosition() + "");

                // 是当前播放的音乐
                if (MusicPlayer.getCurPlayingId() == item.getId()) {

                }

                // 弹出 menu对话框
                helper.getView(R.id.more).setOnClickListener(v -> {
                    Dialogs.showMusicItemMenu(mLayoutInflater, item, FallMusicListActivity.this);
                });
            }
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            FallMusic item = mAdapter.getItem(position);

            if (item.getId() == MusicPlayer.getCurPlayingId()) {
                Router.turnTo(mContext, MusicPlayerActivity.class)
                    .start();
            } else {
                MusicPlayer.playNow(item);
            }
        });
//        mAdapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    protected void onLoadData() {
        switchView(LOADING);
        mAdapter.onLoadData(1);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnimHelper.cancel(mDataBind.musicDisk);
    }
}
