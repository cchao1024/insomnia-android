package com.cchao.sleeping.ui.fall;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.model.event.CommonEvent;
import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.sleeping.BR;
import com.cchao.sleeping.R;
import com.cchao.sleeping.api.RetrofitHelper;
import com.cchao.sleeping.databinding.MusicListBinding;
import com.cchao.sleeping.manager.MusicHelper;
import com.cchao.sleeping.model.javabean.RespListBean;
import com.cchao.sleeping.model.javabean.fall.FallMusic;
import com.cchao.sleeping.ui.music.MusicPlayerActivity;
import com.cchao.sleeping.util.AnimHelper;
import com.cchao.sleeping.view.adapter.PageAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzx.musiclibrary.manager.MusicManager;

import io.reactivex.Observable;

/**
 * @author cchao
 * @version 8/12/18.
 */
public class FallMusicListActivity extends BaseToolbarActivity<MusicListBinding> {

    RecyclerView mRecycler;
    PageAdapter<FallMusic> mAdapter;
    int mCurSongId = 0;

    @Override
    protected int getLayout() {
        return R.layout.music_list;
    }

    @Override
    protected void initEventAndData() {
        initAdapter();
        initEvent();
        onLoadData();
        mDataBinding.setClicker(v -> {
            switch (v.getId()) {
                case R.id.music_disk:
                    Router.turnTo(mContext, MusicPlayerActivity.class).start();
                    break;

            }
        });
    }

    private void initEvent() {
        addSubscribe(RxBus.getDefault().toObservable(new RxBus.CommonCodeCallBack() {
            @Override
            public void onConsumer(CommonEvent commonEvent) {
                switch (commonEvent.getCode()) {
                    case MusicManager.MSG_PLAYER_START:
                        mDataBinding.musicDisk.setVisibility(View.VISIBLE);
                        AnimHelper.startRotate(mDataBinding.musicDisk);
                        mCurSongId = Integer.parseInt(MusicManager.get().getCurrPlayingMusic().getSongId());
                        break;
                    case MusicManager.MSG_PLAYER_STOP:
                    case MusicManager.MSG_PLAYER_PAUSE:
                        mDataBinding.musicDisk.setVisibility(View.GONE);
                        mCurSongId = 0;
                        break;
                }

                for (FallMusic music : mAdapter.getData()) {
                    music.isPlaying.set(music.getId() == mCurSongId);
                }
            }
        }));
    }

    private void initAdapter() {
        mRecycler = mDataBinding.recyclerView;
        mDataBinding.refreshLayout.setEnabled(false);

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

                helper.getView(R.id.more_option).setOnClickListener(v -> {
                    MusicHelper.addToPlayList(item);
                    showText("已加入播放列表");
                });
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FallMusic item = mAdapter.getItem(position);

                MusicHelper.playNow(item);

                if (mCurSongId == item.getId()) {
                    Router.turnTo(mContext, MusicPlayerActivity.class)
                        .start();
                }
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
        AnimHelper.cancel(mDataBinding.musicDisk);
    }
}
