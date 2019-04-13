package com.cchao.insomnia.ui.post;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.databinding.CommonRecyclerBinding;
import com.cchao.insomnia.databinding.PostBoxBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.model.javabean.RespListBean;
import com.cchao.insomnia.model.javabean.post.PostListVO;
import com.cchao.insomnia.view.adapter.PageAdapter;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.ui.fragment.BaseStatefulFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;

import io.reactivex.Observable;

/**
 * 聊天室
 *
 * @author : cchao
 * @version 2019-04-05
 */
public class PostBoxFragment extends BaseStatefulFragment<PostBoxBinding> implements View.OnClickListener {

    PageAdapter<PostListVO> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.post_box;
    }

    @Override
    protected void initEventAndData() {
        mDataBind.setClicker(this);
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

        mDataBind.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

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
    protected void onLoadData() {
        switchView(LOADING);
        mAdapter.onLoadData(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_action:
                Router.turnTo(mContext, AddPostActivity.class).start();
                break;
        }
    }
}
