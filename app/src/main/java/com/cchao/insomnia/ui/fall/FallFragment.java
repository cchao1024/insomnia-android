package com.cchao.insomnia.ui.fall;

import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.cchao.insomnia.BR;
import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.databinding.FallFragmentBinding;
import com.cchao.insomnia.databinding.FallHeadBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.manager.MusicHelper;
import com.cchao.insomnia.model.javabean.RespListBean;
import com.cchao.insomnia.model.javabean.fall.FallImage;
import com.cchao.insomnia.model.javabean.fall.FallMusic;
import com.cchao.insomnia.ui.global.ImageShowActivity;
import com.cchao.insomnia.ui.music.MusicPlayerActivity;
import com.cchao.insomnia.util.AnimHelper;
import com.cchao.insomnia.util.ImageHelper;
import com.cchao.insomnia.view.GridSpacingItemDecoration;
import com.cchao.insomnia.view.adapter.DataBindQuickAdapter;
import com.cchao.insomnia.view.adapter.PageAdapter;
import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.fragment.BaseStatefulFragment;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplelib.util.StringHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzx.musiclibrary.manager.MusicManager;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observable;

/**
 * @author cchao
 * @version 8/10/18.
 */
public class FallFragment extends BaseStatefulFragment<FallFragmentBinding> implements View.OnClickListener {

    RecyclerView mRvMusic;
    RecyclerView mRvImage;
    RecyclerView mRvNature;

    DataBindQuickAdapter<FallMusic> mMusicAdapter;
    PageAdapter<FallImage> mImageAdapter;
    FallHeadBinding mHeadBinding;
    View mMusicDisk;

    @Override
    public void onResume() {
        super.onResume();
        updateDiskView();
    }

    void updateDiskView() {
        if (MusicManager.isPlaying()) {
            if (MusicManager.get().getCurrPlayingMusic() == null) {
                return;
            }
            mMusicDisk.setVisibility(View.VISIBLE);
            AnimHelper.startRotate(mMusicDisk);
        } else {
            mMusicDisk.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fall_fragment;
    }

    @Override
    protected void initEventAndData() {
        initView();
        initEvent();
        initImageAdapter();
        initMusicAdapter();
        onLoadData();
    }


    private void initView() {
        mHeadBinding = DataBindingUtil.inflate(mLayoutInflater
            , R.layout.fall_head, null, false);
        mRvMusic = mHeadBinding.rvMusic;
        mRvNature = mHeadBinding.rvNature;

        mRvImage = mDataBind.rvImage;
        mMusicDisk = mDataBind.musicDisk;

        mHeadBinding.setClicker(this);
        mDataBind.setClicker(this);
    }

    private void initEvent() {
        addSubscribe(RxBus.getDefault().toObservable(commonEvent -> {
            switch (commonEvent.getCode()) {
                case Constants.Event.Update_Play_Status:
                    updateDiskView();
                    break;
            }
        }));
    }

    private void initMusicAdapter() {
        mRvMusic.setNestedScrollingEnabled(false);
        mRvMusic.setLayoutManager(new GridLayoutManager(mContext, 3));

        mRvMusic.addItemDecoration(new GridSpacingItemDecoration(3, UiHelper.dp2px(10), true));

        mRvMusic.setAdapter(mMusicAdapter = new DataBindQuickAdapter<FallMusic>(R.layout.fall_music_item) {
            @Override
            protected void convert(DataBindViewHolder helper, FallMusic item) {
                helper.getBinding().setVariable(BR.item, item);
                if (StringHelper.isNotEmpty(item.getCover_img())) {
                    ImageLoader.loadImage(helper.getView(R.id.image), item.getSrc());
                } else {
                    ImageLoader.loadImage(helper.getView(R.id.image), Constants.TEST_IMAGE_PATH);
                }
            }
        });
        mMusicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FallMusic item = mMusicAdapter.getItem(position);
                if (StringUtils.equals(item.getId(), MusicHelper.getCurPlayingId())) {
                    Router.turnTo(mContext, MusicPlayerActivity.class)
                        .start();
                } else {
                    MusicHelper.playNow(item);
                }
            }
        });
    }

    private void initImageAdapter() {

        mRvImage.setNestedScrollingEnabled(false);
        mRvImage.addItemDecoration(new GridSpacingItemDecoration(2, UiHelper.dp2px(10), true) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildViewHolder(view).getItemViewType() == BaseQuickAdapter.HEADER_VIEW) {
                    outRect.setEmpty();
                    return;
                }
                super.getItemOffsets(outRect, view, parent, state);
            }
        });

        mRvImage.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRvImage.setAdapter(mImageAdapter = new PageAdapter<FallImage>(R.layout.fall_image_item
            , mDisposable, this) {

            int itemWidth = UiHelper.getScreenWidth() / 2;

            @Override
            protected Observable<RespListBean<FallImage>> getLoadObservable(int page) {
                return RetrofitHelper.getApis().getImageList(page);
            }

            @Override
            protected void convert(DataBindViewHolder helper, FallImage item) {
                helper.itemView.getLayoutParams().width = itemWidth;
                helper.itemView.getLayoutParams().height = ImageHelper
                    .getScaleHeight(itemWidth, item.getWidth(), item.getHeight());

                ImageLoader.loadImage(helper.getView(R.id.image), item.getUrl());
            }
        });
        mImageAdapter.setOnItemClickListener((adapter, view, position) -> {
            Router.turnTo(mContext, ImageShowActivity.class)
                .putExtra(Constants.Extra.IMAGE_URL, mImageAdapter.getData().get(position).getUrl())
                .start();
        });
        mImageAdapter.addHeaderView(mHeadBinding.getRoot());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AnimHelper.cancel(mMusicDisk);
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
            case R.id.music_disk:
                Router.turnTo(mContext, MusicPlayerActivity.class).start();
                break;
            default:
                break;
        }
    }
}
