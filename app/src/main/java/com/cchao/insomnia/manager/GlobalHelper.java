package com.cchao.insomnia.manager;

import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.model.javabean.RespBean;
import com.cchao.insomnia.model.javabean.global.AppLaunch;
import com.cchao.simplelib.core.RxHelper;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 处理全局的业务任务
 *
 * @author : cchao
 * @version 2019-04-14
 */
public class GlobalHelper {

    /**
     * 启动同步获取app信息
     */
    public static void syncAppInfo() {
        Disposable disposable = RetrofitHelper.getApis().appLaunch()
            .compose(RxHelper.rxSchedulerTran())
            .subscribe(new Consumer<RespBean<AppLaunch>>() {
                @Override
                public void accept(RespBean<AppLaunch> respBean) throws Exception {
                    UserManager.setUserBean(respBean.getData().getUserInfo());
                }
            });
    }
}
