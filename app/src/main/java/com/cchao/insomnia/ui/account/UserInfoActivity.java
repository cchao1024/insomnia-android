package com.cchao.insomnia.ui.account;

import android.view.View;

import com.cchao.insomnia.R;
import com.cchao.insomnia.databinding.UserInfoActivityBinding;
import com.cchao.insomnia.manager.UserManager;
import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;

/**
 * 用户信息
 *
 * @author cchao
 * @version 2019-05-08.
 */
public class UserInfoActivity extends BaseTitleBarActivity<UserInfoActivityBinding> implements View.OnClickListener {
    @Override
    protected int getLayout() {
        return R.layout.user_info_activity;
    }

    @Override
    protected void initEventAndData() {
        setTitleBarVisible(false);
        mDataBind.setClick(this);
        mDataBind.setBean(UserManager.getUserBean());
        ImageLoader.loadImageCircle(mDataBind.avatar, UserManager.getUserBean().getAvatar(), R.drawable.default_portrait);
    }

    @Override
    protected void onLoadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_user_info:
                Router.turnTo(mContext, EditUserInfoActivity.class).start();
                break;
        }
    }
}
