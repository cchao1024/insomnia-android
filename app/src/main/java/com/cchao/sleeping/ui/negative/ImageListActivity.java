package com.cchao.sleeping.ui.negative;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;

import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.simplelib.util.DeviceInfo;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplelib.util.UIUtils;
import com.cchao.sleeping.R;
import com.cchao.sleeping.api.RetrofitHelper;
import com.cchao.sleeping.databinding.CommonRecyclerBinding;
import com.cchao.sleeping.global.Constants;
import com.cchao.sleeping.model.javabean.fall.FallImage;
import com.cchao.sleeping.util.ImageHelper;
import com.cchao.sleeping.view.adapter.DataBindQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * @author cchao
 * @version 8/12/18.
 */
public class ImageListActivity extends BaseToolbarActivity<CommonRecyclerBinding> {

    RecyclerView mRecyclerView;
    DataBindQuickAdapter<FallImage> mAdapter;
    int mCurPage;

    @Override
    protected int getLayout() {
        return R.layout.common_recycler;
    }

    @Override
    protected void initEventAndData() {
        initAdapter();
        onLoadData();
    }

    private void initAdapter() {
        mRecyclerView = mDataBind.recyclerView;
        mDataBind.refreshLayout.setEnabled(false);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter = new DataBindQuickAdapter<FallImage>(R.layout.fall_image_item) {

            int itemWidth = DeviceInfo.getScreenWidth() / 2 - UIUtils.dp2px(15);

            @Override
            protected void convert(DataBindViewHolder helper, FallImage item) {
                ImageView imageView = helper.getView(R.id.image);
//                helper.getBinding().setVariable(BR.item,item);
                helper.itemView.getLayoutParams().height = ImageHelper.getScaleHeight(itemWidth, item.getWidth(), item.getHeight());

                ImageLoader.loadImageCrop(mContext, Constants.TEST_IMAGE_PATH, imageView);
            }
        });
        mAdapter.disableLoadMoreIfNotFullPage();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
    }

    @Override
    protected void onLoadData() {
        switchView(LOADING);
        addSubscribe(RetrofitHelper.getApis().getImageList(1)
            .compose(RxHelper.rxSchedulerTran())
            .subscribe(respBean -> {
                mCurPage = respBean.getCurPage();
                switchView(CONTENT);
                mAdapter.setNewData(respBean.getData());
            }, throwable -> {
                switchView(NET_ERROR);
                ExceptionCollect.logException(throwable);
            }));
    }

    protected void loadMore() {
        addSubscribe(RetrofitHelper.getApis().getImageList(++mCurPage)
            .compose(RxHelper.rxSchedulerTran())
            .subscribe(respBean -> {
                mCurPage = respBean.getCurPage();
                mAdapter.addData(respBean.getData());
                if (mCurPage == respBean.getTotalPage()) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                }
            }, throwable -> {
                mAdapter.loadMoreFail();
                ExceptionCollect.logException(throwable);
            }));
    }
}
