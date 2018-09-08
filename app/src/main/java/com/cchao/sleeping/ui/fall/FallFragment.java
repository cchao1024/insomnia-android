package com.cchao.sleeping.ui.fall;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.model.event.CommonEvent;
import com.cchao.simplelib.ui.fragment.SimpleLazyFragment;
import com.cchao.simplelib.util.DeviceInfo;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplelib.util.StringHelper;
import com.cchao.sleeping.BR;
import com.cchao.sleeping.R;
import com.cchao.sleeping.api.RetrofitHelper;
import com.cchao.sleeping.databinding.FallFragmentBinding;
import com.cchao.sleeping.global.Constants;
import com.cchao.sleeping.model.javabean.fall.FallImage;
import com.cchao.sleeping.model.javabean.fall.FallMusic;
import com.cchao.sleeping.ui.global.ImageShowActivity;
import com.cchao.sleeping.ui.music.MusicPlayerActivity;
import com.cchao.sleeping.util.ImageHelper;
import com.cchao.sleeping.view.GridSpacingItemDecoration;
import com.cchao.sleeping.view.adapter.DataBindQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * @author cchao
 * @version 8/10/18.
 */
public class FallFragment extends SimpleLazyFragment<FallFragmentBinding> implements View.OnClickListener {

    RecyclerView mRvMusic;
    RecyclerView mRvImage;
    RecyclerView mRvNature;

    DataBindQuickAdapter<FallMusic> mMusicAdapter;
    BaseQuickAdapter<FallImage, BaseViewHolder> mImageAdapter;

    View mMusicDisk;

    @Override
    public void onFirstUserVisible() {
        findViews();
        initEvent();
        initMusicAdapter();
        initImageAdapter();
        onLoadData();
    }

    private void initEvent() {
        addSubscribe(RxBus.getDefault().toObservable(new RxBus.CommonCodeCallBack() {
            @Override
            public void onConsumer(CommonEvent commonEvent) {
                switch (commonEvent.getCode()) {
                    case MusicManager.MSG_PLAYER_START:
                        mMusicDisk.setVisibility(View.VISIBLE);
                        break;
                    case MusicManager.MSG_PLAYER_STOP:
                        mMusicDisk.setVisibility(View.GONE);
                        break;
                }
            }
        }));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fall_fragment;
    }

    private void findViews() {
        mRvMusic = mDataBind.rvMusic;
        mRvImage = mDataBind.rvImage;
        mRvNature = mDataBind.rvNature;
        mMusicDisk = mDataBind.musicDisk;

        mDataBind.setClick(this);
    }

    private void initMusicAdapter() {
        mRvMusic.setNestedScrollingEnabled(false);
        mRvMusic.setLayoutManager(new GridLayoutManager(mContext, 3));

        mRvMusic.addItemDecoration(new GridSpacingItemDecoration(3, UiHelper.dp2dx(10), true));

        mRvMusic.setAdapter(mMusicAdapter = new DataBindQuickAdapter<FallMusic>(R.layout.fall_music_item) {
            @Override
            protected void convert(DataBindViewHolder helper, FallMusic item) {
                helper.getBinding().setVariable(BR.item, item);
                if (StringHelper.isNotEmpty(item.getCover_img())) {
                    ImageLoader.loadImage(mContext, item.getSrc(), helper.getView(R.id.image));
                } else {
                    ImageLoader.loadImage(mContext, Constants.TEST_IMAGE_PATH, helper.getView(R.id.image));
                }
            }
        });
        mMusicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FallMusic item = mMusicAdapter.getItem(position);

                SongInfo songInfo = new SongInfo();
                songInfo.setSongId(String.valueOf(item.getId()));
                songInfo.setSongUrl(item.getSrc());
                MusicManager.get().playMusicByInfo(songInfo);

                Router.turnTo(mContext, MusicPlayerActivity.class)
                    .start();
            }
        });
    }

    private void initImageAdapter() {
        mRvImage.setNestedScrollingEnabled(false);
        mRvImage.addItemDecoration(new GridSpacingItemDecoration(2, UiHelper.dp2dx(10), true));

        mRvImage.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRvImage.setAdapter(mImageAdapter = new BaseQuickAdapter<FallImage, BaseViewHolder>(R.layout.fall_image_item) {

            int itemWidth = DeviceInfo.getScreenWidth() / 2 - UiHelper.dp2dx(15);

            @Override
            protected void convert(BaseViewHolder helper, FallImage item) {
                helper.itemView.getLayoutParams().width = itemWidth;
                helper.itemView.getLayoutParams().height = ImageHelper
                    .getScaleHeight(itemWidth, item.getWidth(), item.getHeight());

                ImageLoader.loadImage(mContext, item.getUrl(), helper.getView(R.id.image));
            }
        });
        mImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Router.turnTo(mContext, ImageShowActivity.class)
                    .putExtra(Constants.Extra.IMAGE_URL, mImageAdapter.getData().get(position).getUrl())
                    .start();
            }
        });
    }

    @Override
    protected void onLoadData() {
        switchView(LOADING);
        addSubscribe(RetrofitHelper.getApis().getIndexData()
            .compose(RxHelper.rxSchedulerTran())
            .subscribe(respBean -> {
                switchView(CONTENT);
                mImageAdapter.setNewData(respBean.getData().getFallimages());
                mMusicAdapter.setNewData(respBean.getData().getMusic());
            }, throwable -> {
                switchView(NET_ERROR);
                ExceptionCollect.logException(throwable);
            }));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_more:
                Router.turnTo(mContext, FallMusicListActivity.class)
                    .start();
                break;
            case R.id.image_more:
                Router.turnTo(mContext, FallImageActivity.class)
                    .start();
                break;
            default:
                break;
        }
    }
}
