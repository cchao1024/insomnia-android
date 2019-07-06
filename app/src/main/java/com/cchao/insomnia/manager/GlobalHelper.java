package com.cchao.insomnia.manager;

import android.content.Context;
import android.content.Intent;

import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.model.javabean.RespBean;
import com.cchao.insomnia.model.javabean.global.AppLaunch;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;

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
    public static void syncAppInfo(Consumer<RespBean<AppLaunch>> consumer, Consumer<Throwable> throwableConsumer) {
        Disposable disposable = RetrofitHelper.getApis().appLaunch()
            .compose(RxHelper.toMain())
            .subscribe(respBean -> {
                UserManager.setUserBean(respBean.getData().getUserInfo());
                if (consumer != null) {
                    consumer.accept(respBean);
                }
            }, throwableConsumer);
    }

    public static void shareImage(Context context, String uri) {
        String title= UiHelper.getString(R.string.share_image_title);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static void shareMusic(Context context, String uri) {
        String title= UiHelper.getString(R.string.share_image_title);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }
}
