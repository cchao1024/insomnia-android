package com.cchao.insomnia.ui.post;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.cchao.insomnia.BR;
import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.databinding.PostDetailActivityBinding;
import com.cchao.insomnia.databinding.PostDetailCommentBinding;
import com.cchao.insomnia.databinding.PostDetailHeadBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.model.javabean.post.CommentVO;
import com.cchao.insomnia.model.javabean.post.PostVO;
import com.cchao.insomnia.model.javabean.post.ReplyVO;
import com.cchao.insomnia.ui.post.convert.CommentConvert;
import com.cchao.insomnia.view.adapter.DataBindQuickAdapter;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 帖子详情页， recycler view， add详情header，
 * recyclerView 的itemBean 是经过转化的，使用统一的类型CommentConvert
 *
 * @author : cchao
 * @version 2019-04-05
 */
public class PostDetailActivity extends BaseTitleBarActivity<PostDetailActivityBinding> implements View.OnClickListener {
    long mId;
    DataBindQuickAdapter<CommentConvert> mAdapter;
    PostVO mPostVO;

    @Override
    protected int getLayout() {
        return R.layout.post_detail_activity;
    }

    @Override
    protected void initEventAndData() {
        mId = getIntent().getLongExtra(Constants.Extra.ID, 0);
        setTitleText("detail");
        mDataBind.setClicker(this);
        initAdapter();
        onLoadData();
    }

    private void initAdapter() {
        mAdapter = new DataBindQuickAdapter<CommentConvert>(R.layout.post_comment_item, null) {
            @Override
            protected void convert(DataBindViewHolder helper, CommentConvert item) {
                helper.getBinding().setVariable(BR.item, item);
                boolean isReply = "reply".equals(item.getContent());
                UiHelper.setVisibleElseGone(helper.getView(R.id.space), isReply);
            }
        };

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        mDataBind.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDataBind.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onLoadData() {
        switchView(LOADING);
        addSubscribe(RetrofitHelper.getApis().getPostDetail(mId)
            .compose(RxHelper.toMain())
            .subscribe(respBean -> {
                if (respBean.isCodeFail()) {
                    showText(respBean.getMsg());
                    switchView(NET_ERROR);
                    return;
                }
                switchView(CONTENT);
                updateDetail(respBean.getData());
                convertData(respBean.getData().getList());
            }, RxHelper.getSwitchErrorConsumer(this)));
    }

    private void convertData(List<CommentVO> list) {
        List<CommentConvert> result = new ArrayList<>();

        for (CommentVO commentVO : list) {
            result.add(CommentConvert.fromCommentVo(commentVO));

            // 遍历reply
            for (ReplyVO replyVO : commentVO.getList()) {
                result.add(CommentConvert.fromReplyVO(replyVO));
            }
        }
        mAdapter.addData(result);
    }

    /**
     * 更新detail
     */
    private void updateDetail(PostVO data) {
        mPostVO = data;
        PostDetailHeadBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.post_detail_head
            , (ViewGroup) mDataBind.getRoot(), false);
        binding.setItem(data);
        mAdapter.addHeaderView(binding.getRoot());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_action:
                showCommentDialog(mPostVO.getPostUserId());
                break;
        }
    }

    /**
     * 显示评论对话框，用户输入，点发送
     *
     * @param toUserId 目标用户id
     */
    private void showCommentDialog(long toUserId) {
        PostDetailCommentBinding binding = DataBindingUtil.inflate(mLayoutInflater
            , R.layout.post_detail_comment, null, false);
        // 弹出对话框
        new AlertDialog.Builder(mContext)
            .setTitle("元芳，你怎么看")
            .setView(binding.getRoot())
            .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (TextUtils.isEmpty(binding.edit.getText().toString())) {
                        showText("内容不能为空");
                    }
                    onSendComment(binding.edit.getText().toString(), toUserId, () -> {
                        dialog.dismiss();
                    });
                }
            }).show();
    }

    /**
     * 发送评论，
     * 如果toUserId == PostUserId 则为评论，否则为回复
     */
    void onSendComment(String content, long toUserId, Runnable runnable) {
        showProgress();
        String type = toUserId == mPostVO.getPostUserId() ? "comment" : "reply";
        addSubscribe(RetrofitHelper.getApis().addCommentOrReply(type, content, "")
            .compose(RxHelper.toMain())
            .subscribe(respBean -> {
                showText(respBean.getMsg());
                if (respBean.isCodeSuc()) {
                    runnable.run();
                }
                hideProgress();
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Logs.logException(throwable);
                    hideProgress();
                }
            }));
    }
}
