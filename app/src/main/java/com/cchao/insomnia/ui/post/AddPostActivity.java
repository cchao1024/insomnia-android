package com.cchao.insomnia.ui.post;

import android.text.TextUtils;
import android.view.View;

import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.databinding.PostAddActivityBinding;
import com.cchao.insomnia.model.javabean.RespBean;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;

/**
 * 编辑 发说说页
 *
 * @author : cchao
 * @version 2019-04-13
 */
public class AddPostActivity extends BaseTitleBarActivity<PostAddActivityBinding> implements View.OnClickListener {

    @Override
    protected int getLayout() {
        return R.layout.post_add_activity;
    }

    @Override
    protected void initEventAndData() {
        setTitleText("冒出个想法");
        addTitleMenuItem(R.drawable.action_send, v -> onSendPost());
        mDataBind.setClicker(this);
    }

    private void onSendPost() {
        if (TextUtils.isEmpty(mDataBind.edit.getText().toString())) {
            showText("请输入内容");
            return;
        }
        showProgress();
        addSubscribe(RetrofitHelper.getApis().addPost(mDataBind.edit.getText().toString(), "")
            .compose(RxHelper.rxSchedulerTran())
            .subscribe(respBean -> {
                hideProgress();
                showText(respBean.getMsg());
                if (respBean.isCodeSuc()) {
                    finish();
                }
            }, RxHelper.getErrorTextConsumer(this)));
    }

    @Override
    protected void onLoadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:

                break;
        }
    }
}
