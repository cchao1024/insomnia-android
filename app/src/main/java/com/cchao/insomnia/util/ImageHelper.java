package com.cchao.insomnia.util;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.model.javabean.RespBean;
import com.cchao.insomnia.model.javabean.post.UploadImageBean;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author cchao
 * @version 8/12/18.
 */
public class ImageHelper {

    public static int getScaleHeight(int containerWidth, int width, int height) {
        return (int) (containerWidth * (float) height / width);
    }

    public static void takeImage(Context context, int maxCount) {
        // 自定义图片加载器
        ISNav.getInstance().init(new com.yuyh.library.imgsel.common.ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });

        // 图片选择
        ISListConfig config = new ISListConfig.Builder()
            // 是否多选, 默认true
            .multiSelect(true)
            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
            .rememberSelected(false)
            // “确定”按钮背景色
            .btnBgColor(Color.GRAY)
            // “确定”按钮文字颜色
            .btnTextColor(Color.BLUE)
            // 使用沉浸式状态栏
            .statusBarColor(Color.parseColor("#3F51B5"))
            // 返回图标ResId
            .backResId(R.drawable.ic_back)
            // 标题
            .title("图片")
            // 标题文字颜色
            .titleColor(Color.WHITE)
            // TitleBar背景色
            .titleBgColor(Color.parseColor("#3F51B5"))
            // 裁剪大小。needCrop为true的时候配置
            .cropSize(1, 1, 200, 200)
            .needCrop(true)
            // 第一个是否显示相机，默认true
            .needCamera(true)
            // 最大选择图片数量，默认9
            .maxNum(maxCount)
            .build();

        // 跳转到图片选择器
        ISNav.getInstance().toListActivity(context, config, Constants.RequestCode.TAKE_IMAGE);
    }

    public static void uploadImage(CompositeDisposable compositeDisposable, UploadImageBean bean
        , Consumer<RespBean<UploadImageBean>> respConsumer, Consumer<? super Throwable> error) {

        File file = new File(bean.getLocalUri());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        // api 上传
        compositeDisposable.add(RetrofitHelper.getApis().uploadImage(body)
            .compose(RxHelper.toMain())
            .subscribe(respBean -> {
                if (respBean.isCodeFail()) {
                    UiHelper.showToast(respBean.getMsg());
                    return;
                }

                bean.setAbsoluteUrl(respBean.getData().getAbsoluteUrl())
                    .setRelativeUrl(respBean.getData().getRelativeUrl());

                if (respConsumer != null) {
                    respConsumer.accept(respBean);
                }
            }, error));
    }
}
