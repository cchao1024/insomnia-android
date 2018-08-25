package com.cchao.sleeping.ui.fall;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.ui.fragment.SimpleLazyFragment;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplelib.util.StringHelper;
import com.cchao.simplemusic.IMusicPlayer;
import com.cchao.simplemusic.MusicService;
import com.cchao.simplemusic.model.MusicItem;
import com.cchao.sleeping.BR;
import com.cchao.sleeping.R;
import com.cchao.sleeping.api.RetrofitHelper;
import com.cchao.sleeping.databinding.NegativeFragmentBinding;
import com.cchao.sleeping.global.Constants;
import com.cchao.sleeping.model.javabean.fall.FallImage;
import com.cchao.sleeping.model.javabean.fall.FallMusic;
import com.cchao.sleeping.ui.global.ImageShowActivity;
import com.cchao.sleeping.ui.music.MusicPlayerActivity;
import com.cchao.sleeping.ui.negative.ImageListActivity;
import com.cchao.sleeping.view.adapter.DataBindQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.net.URLEncoder;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * @author cchao
 * @version 8/10/18.
 */
public class FallFragment extends SimpleLazyFragment<NegativeFragmentBinding> implements View.OnClickListener {

    RecyclerView mRvMusic;
    RecyclerView mRvImage;
    RecyclerView mRvNature;

    DataBindQuickAdapter<FallMusic> mMusicAdapter;
    BaseQuickAdapter<FallImage, BaseViewHolder> mImageAdapter;

    ServiceConnection mServiceConnection;
    IMusicPlayer mMusicPlayer;

    @Override
    public void onFirstUserVisible() {
        findViews();
        initMusicAdapter();
        initImageAdapter();
        onLoadData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.negative_fragment;
    }

    private void findViews() {
        mRvMusic = mDataBind.rvMusic;
        mRvImage = mDataBind.rvImage;
        mRvNature = mDataBind.rvNature;

        mDataBind.setClick(this);
    }

    private void initMusicAdapter() {
        mRvMusic.setNestedScrollingEnabled(false);
        mRvMusic.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRvMusic.setAdapter(mMusicAdapter = new DataBindQuickAdapter<FallMusic>(R.layout.negative_item_music) {
            @Override
            protected void convert(DataBindViewHolder helper, FallMusic item) {
                helper.getBinding().setVariable(BR.item, item);
                if (StringHelper.isNotEmpty(item.getCover_img())) {
                    ImageLoader.loadImageCrop(mContext, item.getSrc(), helper.getView(R.id.image));
                } else {
                    ImageLoader.loadImageCrop(mContext, Constants.TEST_IMAGE_PATH, helper.getView(R.id.image));
                }
            }
        });
        mMusicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FallMusic fallMusic = mMusicAdapter.getItem(position);

                MusicItem item = new MusicItem(fallMusic.getId() + ""
                    , URLEncoder.encode(fallMusic.getSrc())`, fallMusic.getName());

                try {
                    if (mMusicPlayer == null) {
                        initPlayService(item);
                    } else {
                        mMusicPlayer.stop();
                        mMusicPlayer.enqueue(item);
                        mMusicPlayer.play();
                    }
                } catch (RemoteException e) {
                    ExceptionCollect.logException(e);
                }
                Router.turnTo(mContext, MusicPlayerActivity.class)
                    .start();
            }
        });
    }

    void initPlayService(MusicItem item) {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMusicPlayer = (IMusicPlayer) service;
                try {
                    mMusicPlayer.enqueue(item);
                    mMusicPlayer.play();
                } catch (RemoteException ex) {
                    ExceptionCollect.logException(ex);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void initImageAdapter() {
        mRvMusic.setNestedScrollingEnabled(false);
        mRvImage.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRvImage.setAdapter(mImageAdapter = new BaseQuickAdapter<FallImage, BaseViewHolder>(R.layout.negative_item_music) {
            @Override
            protected void convert(BaseViewHolder helper, FallImage item) {
                ImageLoader.loadImageCrop(mContext, item.getUrl(), helper.getView(R.id.image));
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
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null) {
            getActivity().unbindService(mServiceConnection);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_more:
                break;
            case R.id.image_more:
                Router.turnTo(mContext, ImageListActivity.class)
                    .start();
                break;
            default:
                break;
        }
    }
}
