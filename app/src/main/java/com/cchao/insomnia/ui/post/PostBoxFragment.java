package com.cchao.insomnia.ui.post;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.databinding.PostBoxBinding;
import com.cchao.insomnia.databinding.PostDetailCommentBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.model.javabean.RespListBean;
import com.cchao.insomnia.model.javabean.post.PostListVO;
import com.cchao.insomnia.util.TimeHelper;
import com.cchao.insomnia.view.adapter.PageAdapter;
import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
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
                ImageLoader.loadImageCircle(helper.getView(R.id.avatar), item.getPostUserAvatar(), R.drawable.default_portrait);
                helper.setText(R.id.date, TimeHelper.getStandardDate(item.getUpdateTime()));
                helper.setOnClickListener(R.id.comment_button, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCommentDialog(item.getId());
                    }
                });
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

    /**
     * 显示评论对话框，用户输入，点发送
     *
     * @param id 评论id
     */
    private void showCommentDialog(long id) {
        PostDetailCommentBinding binding = DataBindingUtil.inflate(mLayoutInflater
            , R.layout.post_detail_comment, null, false);

        // 弹出对话框
        Dialog dialog = new AlertDialog.Builder(mContext)
            .setTitle("元芳，你怎么看")
            .setView(binding.getRoot())
            .show();

        binding.send.setOnClickListener(click -> {
            if (TextUtils.isEmpty(binding.edit.getText().toString())) {
                showText("内容不能为空");
            }
            onSendComment(binding, id, () -> {
                dialog.dismiss();
            });
        });
    }

    /**
     * 发送评论，
     */
    void onSendComment(PostDetailCommentBinding binding, long id, Runnable callback) {
        UiHelper.setVisibleElseGone(binding.send, false);
        UiHelper.setVisibleElseGone(binding.progress, true);

        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("toId", String.valueOf(id));
        map.put("content", binding.edit.getText().toString());
        map.put("images", "");

        addSubscribe(RetrofitHelper.getApis().addCommentOrReply("comment", map)
            .compose(RxHelper.toMain())
            .subscribe(respBean -> {
                showText(respBean.getMsg());
                if (respBean.isCodeSuc()) {
                    callback.run();
                }

                UiHelper.setVisibleElseGone(binding.send, true);
                UiHelper.setVisibleElseGone(binding.progress, false);
            }, throwable -> {
                Logs.logException(throwable);
                showError();

                UiHelper.setVisibleElseGone(binding.send, true);
                UiHelper.setVisibleElseGone(binding.progress, false);
            }));
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
