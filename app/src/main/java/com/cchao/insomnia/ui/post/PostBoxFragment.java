package com.cchao.insomnia.ui.post;

import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.databinding.CommonRecyclerBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.model.javabean.RespListBean;
import com.cchao.insomnia.model.javabean.post.PostListVO;
import com.cchao.insomnia.view.adapter.PageAdapter;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.ui.fragment.SimpleLazyFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;

import io.reactivex.Observable;

/**
 * 聊天室
 *
 * @author : cchao
 * @version 2019-04-05
 */
public class PostBoxFragment extends SimpleLazyFragment<CommonRecyclerBinding> {

    PageAdapter<PostListVO> mAdapter;

    @Override
    public void onFirstUserVisible() {
        initAdapter();
        onLoadData();
    }

    private void initAdapter() {

        mDataBind.recyclerView.setAdapter(mAdapter = new PageAdapter<PostListVO>
            (R.layout.post_box_item, mDisposable, this) {

            @Override
            protected Observable<RespListBean<PostListVO>> getLoadObservable(int page) {
                return RetrofitHelper.getApis().getPostBoxList(page);
            }

            @Override
            protected void convert(DataBindViewHolder helper, PostListVO item) {
                helper.getBinding().setVariable(BR.item, item);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Router.turnTo(mContext, PostDetailActivity.class)
                    .putExtra(Constants.Extra.ID, mAdapter.getData().get(position).getId())
                    .start();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_recycler;
    }

    @Override
    protected void onLoadData() {
        switchView(LOADING);
        mAdapter.onLoadData(1);
    }
}
