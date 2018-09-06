package com.cchao.sleeping.view.adapter;

import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.interfaces.BaseStateView;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.sleeping.model.javabean.RespListBean;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @author cchao
 * @version 8/11/18.
 */
public abstract class PageAdapter<T> extends DataBindQuickAdapter<T> {

    int mCurPage = 1;
    CompositeDisposable mDisposable;
    BaseStateView mStateView;

    public PageAdapter(int layoutResId, CompositeDisposable disposable, BaseStateView stateView) {
        super(layoutResId);
        mDisposable = disposable;
        mStateView = stateView;
        init();
    }

    protected abstract Observable<RespListBean<T>> getLoadObservable(int page);

    protected void init() {
        setOnLoadMoreListener(new RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                onLoadData(++mCurPage);
            }
        });
    }

    public void onLoadData(int page) {
        mDisposable.add(getLoadObservable(page)
            .compose(RxHelper.rxSchedulerTran())
            .subscribe(new Consumer<RespListBean<T>>() {
                @Override
                public void accept(RespListBean<T> respBean) throws Exception {
                    solvePage(respBean);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    ExceptionCollect.logException(throwable);
                    if (page == 1 && getData().size() == 0) {
                        mStateView.switchView(BaseStateView.NET_ERROR);
                    } else {
                        UiHelper.showToast(throwable.getMessage());
                    }
                }
            }));
    }

    public void solvePage(RespListBean<T> respBean) {
        if (!respBean.isCodeSuc()) {
            UiHelper.showToast(respBean.getMsg());
            loadMoreComplete();
            return;
        }
        mStateView.switchView(BaseStateView.CONTENT);
        List<T> data = respBean.getData();

        mCurPage = respBean.getCurPage();
        if (mCurPage == 1) {
            setNewData(data);
        } else {
            addData(data);
        }
        // 是否最后一页
        if (mCurPage == respBean.getTotalPage()) {
            loadMoreEnd();
        } else {
            loadMoreComplete();
        }
    }
}
