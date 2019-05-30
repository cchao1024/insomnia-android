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
    public static void syncAppInfo(Consumer<RespBean<AppLaunch>> consumer,Consumer<Throwable> throwableConsumer) {
        Disposable disposable = RetrofitHelper.getApis().appLaunch()
            .compose(RxHelper.toMain())
            .subscribe(respBean -> {
                UserManager.setUserBean(respBean.getData().getUserInfo());
                if (consumer != null) {
                    consumer.accept(respBean);
                }
            }, throwableConsumer);
    }
}
