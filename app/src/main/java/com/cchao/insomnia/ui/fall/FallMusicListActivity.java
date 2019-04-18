package com.cchao.insomnia.ui.fall;

import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.insomnia.BR;
import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.databinding.MusicItemMenuListBinding;
import com.cchao.insomnia.databinding.MusicListBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.manager.MusicHelper;
import com.cchao.insomnia.model.javabean.RespListBean;
import com.cchao.insomnia.model.javabean.fall.FallMusic;
import com.cchao.insomnia.ui.music.MusicPlayerActivity;
import com.cchao.insomnia.util.AnimHelper;
import com.cchao.insomnia.view.adapter.PageAdapter;
import com.lzx.musiclibrary.manager.MusicManager;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observable;

/**
 * @author cchao
 * @version 8/12/18.
 */
public class FallMusicListActivity extends BaseToolbarActivity<MusicListBinding> {

    RecyclerView mRecycler;
    PageAdapter<FallMusic> mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.music_list;
    }

    @Override
    protected void initEventAndData() {
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
        addSubscribe(RxBus.getDefault().toObservable(commonEvent -> {
            switch (commonEvent.getCode()) {
                case Constants.Event.Update_Play_Status:
                    updateDiskView();

                    for (FallMusic music : mAdapter.getData()) {
                        music.isPlaying.set(StringHelper.equals(music.getId(), MusicHelper.getCurPlayingId()));
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
                if (MusicHelper.getCurPlayingId().equals(item.getId())) {

                }

                // 弹出 menu对话框
                helper.getView(R.id.more_option).setOnClickListener(v -> {
                    showItemMenu(item);
                });
            }
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            FallMusic item = mAdapter.getItem(position);

            if (StringHelper.equals(item.getId(), MusicHelper.getCurPlayingId())) {
                Router.turnTo(mContext, MusicPlayerActivity.class)
                    .start();
            } else {
                MusicHelper.playNow(item);
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

    /**
     * 弹出更多的选项
     */
    void showItemMenu(FallMusic item) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        MusicItemMenuListBinding binding = DataBindingUtil.inflate(mLayoutInflater
            , R.layout.music_item_menu_list, null, false);
        binding.name.setText("歌曲：" + item.getName());
        binding.setClicker(click -> {
            switch (click.getId()) {
                case R.id.next_play:
                    MusicHelper.addToPlayList(item);
                    showText("已加入播放列表");
                    dialog.dismiss();
                    break;
                case R.id.wish:
                    showText("加入wish");
                    dialog.dismiss();
                    break;
                case R.id.download:
                    showText("download");
                    dialog.dismiss();
                    break;
                case R.id.share:
                    dialog.dismiss();
                    break;
                case R.id.info:
                    dialog.dismiss();
                    break;
            }
        });

        dialog.setContentView(binding.getRoot());
        dialog.show();
    }
}
