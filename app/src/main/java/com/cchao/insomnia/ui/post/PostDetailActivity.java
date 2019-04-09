package com.cchao.insomnia.ui.post;

import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.model.javabean.post.PostVO;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.ui.activity.BaseToolbarActivity;

/**
 * @author : cchao
 * @version 2019-04-05
 */
public class PostDetailActivity extends BaseToolbarActivity {
    long mId;

    @Override
    protected int getLayout() {
        return R.layout.post_detail_activity;
    }

    @Override
    protected void initEventAndData() {
        mId = getIntent().getLongExtra(Constants.Extra.ID, 0);
    }

    @Override
    protected void onLoadData() {
        switchView(LOADING);
        addSubscribe(RetrofitHelper.getApis().getPostDetail(mId)
            .compose(RxHelper.rxSchedulerTran())
            .subscribe(respBean -> {
                if (respBean.isCodeFail()) {
                    showText(respBean.getMsg());
                    switchView(NET_ERROR);
                    return;
                }
                switchView(CONTENT);
                updateDetail(respBean.getData());
            }, RxHelper.getSwitchErrorConsumer(this)));
    }

    private void updateDetail(PostVO data) {

    }
}
