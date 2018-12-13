package com.cchao.sleeping.ui.global;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.sleeping.R;
import com.cchao.sleeping.databinding.CommonRecyclerBinding;
import com.cchao.sleeping.model.javabean.RespListBean;
import com.cchao.sleeping.view.adapter.DataBindQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author cchao
 * @version 8/12/18.
 */
public abstract class BaseListActivity<E> extends BaseToolbarActivity<CommonRecyclerBinding> {

    protected RecyclerView mRecyclerView;
    protected DataBindQuickAdapter<E> mAdapter;
    protected int mCurPage;

    @Override
    protected int getLayout() {
        return R.layout.common_recycler;
    }

    @Override
    protected void initEventAndData() {
        initAdapter();
        onLoadData();
    }

    protected abstract void onConvertItem(DataBindQuickAdapter.DataBindViewHolder helper, E item);

    protected abstract void onItemClicked(BaseQuickAdapter adapter, View view, int position);

    protected abstract Observable<RespListBean<E>> getLoadObservable(int page);

    protected void initAdapter() {
        mRecyclerView = mDataBind.recyclerView;
        mDataBind.refreshLayout.setEnabled(false);
        mRecyclerView.setAdapter(mAdapter = new DataBindQuickAdapter<E>(R.layout.fall_image_item) {

            @Override
            protected void convert(DataBindViewHolder helper, E item) {
                onConvertItem(helper, item);
            }
        });
//        mAdapter.disableLoadMoreIfNotFullPage();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemClicked(adapter, view, position);
            }
        });
    }

    public void solvePage(RespListBean<E> respBean) {
        List<E> data = respBean.getData();
        // 为空
        if (mCurPage == 1 && data == null) {
            switchView(EMPTY);
            return;
        }

        mCurPage = respBean.getCurPage();
        if (mCurPage == 1) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
        // 是否最后一页
        if (mCurPage == respBean.getTotalPage()) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
        switchView(CONTENT);
    }

    @Override
    protected void onLoadData() {
        switchView(LOADING);
        addSubscribe(getLoadObservable(1)
            .compose(RxHelper.rxSchedulerTran())
            .subscribe(new Consumer<RespListBean<E>>() {
                @Override
                public void accept(RespListBean<E> respBean) throws Exception {
                    solvePage(respBean);
                }
            }, RxHelper.getSwitchErrorConsumer(this)));
    }

    protected void loadMore() {
        addSubscribe(getLoadObservable(++mCurPage)
            .compose(RxHelper.rxSchedulerTran())
            .subscribe(new Consumer<RespListBean<E>>() {
                @Override
                public void accept(RespListBean<E> respBean) throws Exception {
                    solvePage(respBean);
                }
            }, RxHelper.getSwitchErrorConsumer(this)));
    }
}
