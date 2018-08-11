package com.cchao.sleeping.ui.positive;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.ui.fragment.SimpleLazyFragment;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplelib.util.StringHelper;
import com.cchao.sleeping.BR;
import com.cchao.sleeping.R;
import com.cchao.sleeping.api.RetrofitHelper;
import com.cchao.sleeping.databinding.NegativeFragmentBinding;
import com.cchao.sleeping.model.javabean.fall.FallImage;
import com.cchao.sleeping.model.javabean.fall.FallMusic;
import com.cchao.sleeping.view.adapter.DatabindQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author cchao
 * @version 8/10/18.
 */
public class NegativeFragment extends SimpleLazyFragment<NegativeFragmentBinding> {
    RecyclerView mRvMusic;
    RecyclerView mRvImage;
    RecyclerView mRvNature;

    DatabindQuickAdapter<FallMusic> mMusicAdapter;
    BaseQuickAdapter<FallImage, BaseViewHolder> mImageAdapter;

    @Override
    public void onFirstUserVisible() {
        findViews();
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

        mRvMusic.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mRvMusic.setAdapter(mMusicAdapter = new DatabindQuickAdapter<FallMusic>(R.layout.negative_item_music) {
            @Override
            protected void convert(DataBindViewHolder helper, FallMusic item) {
                helper.getBinding().setVariable(BR.item, item);
                if (StringHelper.isNotEmpty(item.getCover_img())) {
                    ImageLoader.loadImageCrop(mContext, item.getSrc(), helper.getView(R.id.image));
                } else {
                    ImageLoader.loadImageCrop(mContext, "http://d6.yihaodianimg.com/V00/M00/3E/5C/CgQDslSNDEyAQp-mAAHoVWDzhu877700_380x380.jpg", helper.getView(R.id.image));
                }
            }
        });

        mRvImage.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mRvImage.setAdapter(mImageAdapter = new BaseQuickAdapter<FallImage, BaseViewHolder>(R.layout.negative_item_music) {
            @Override
            protected void convert(BaseViewHolder helper, FallImage item) {
                ImageLoader.loadImageCrop(mContext, item.getUrl(), helper.getView(R.id.image));
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
}
