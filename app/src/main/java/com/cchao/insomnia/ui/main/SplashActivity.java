package com.cchao.insomnia.ui.main;

import com.cchao.insomnia.R;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.manager.GlobalHelper;
import com.cchao.simplelib.core.PrefHelper;
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
        GlobalHelper.syncAppInfo(appLaunchRespBean -> {
            RxHelper.timerConsumer(1000, consumer -> {
                Router.turnTo(mContext, MainActivity.class).start();
                finish();
            });
        });

        if (!PrefHelper.contains(Constants.Prefs.INIT_TIME_STAMP)) {
            PrefHelper.putLong(Constants.Prefs.INIT_TIME_STAMP,System.currentTimeMillis());
        }
    }

    @Override
    protected void onLoadData() {

    }
}
