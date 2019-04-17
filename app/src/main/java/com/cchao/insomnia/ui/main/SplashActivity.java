package com.cchao.insomnia.ui.main;

import com.cchao.insomnia.MainActivity;
import com.cchao.insomnia.R;
import com.cchao.insomnia.manager.GlobalHelper;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.ui.activity.BaseStatefulActivity;

/**
 * @author : cchao
 * @version 2019-04-14
 */
public class SplashActivity extends BaseStatefulActivity {
    @Override
    protected int getLayout() {
        return R.layout.splash_activity;
    }

    @Override
    protected void initEventAndData() {
        GlobalHelper.syncAppInfo();
        RxHelper.timerConsumer(1000, consumer -> {
            Router.turnTo(mContext, MainActivity.class).start();
        });
    }

    @Override
    protected void onLoadData() {

    }
}
