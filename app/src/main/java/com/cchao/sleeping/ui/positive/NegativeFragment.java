package com.cchao.sleeping.ui.positive;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.ui.fragment.SimpleLazyFragment;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplelib.util.StringHelper;
import com.cchao.sleeping.BR;
import com.cchao.sleeping.R;
import com.cchao.sleeping.api.RetrofitHelper;
import com.cchao.sleeping.databinding.NegativeFragmentBinding;
import com.cchao.sleeping.global.Constants;
import com.cchao.sleeping.model.javabean.fall.FallImage;
import com.cchao.sleeping.model.javabean.fall.FallMusic;
import com.cchao.sleeping.ui.global.ImageShowActivity;
import com.cchao.sleeping.ui.negative.ImageListActivity;
import com.cchao.sleeping.view.adapter.DataBindQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author cchao
 * @version 8/10/18.
 */
public class NegativeFragment extends SimpleLazyFragment<NegativeFragmentBinding> implements View.OnClickListener {

    RecyclerView mRvMusic;
    RecyclerView mRvImage;
    RecyclerView mRvNature;

    DataBindQuickAdapter<FallMusic> mMusicAdapter;
    BaseQuickAdapter<FallImage, BaseViewHolder> mImageAdapter;

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
