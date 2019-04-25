package com.cchao.insomnia.ui.post;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.databinding.PostAddActivityBinding;
import com.cchao.insomnia.databinding.PostAddImageItemBinding;
import com.cchao.insomnia.model.javabean.post.UploadImageBean;
import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;
import com.google.android.flexbox.FlexboxLayout;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
            .compose(RxHelper.toMain())
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
                showImageSelect();
                break;
        }
    }

    private void showImageSelect() {
        // 自定义图片加载器
        ISNav.getInstance().init(new com.yuyh.library.imgsel.common.ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });

        int maxCount = 6 - mDataBind.pictureGroup.getChildCount();

        // 图片选择
        ISListConfig config2 = new ISListConfig.Builder()
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
        ISNav.getInstance().toListActivity(this, config2, 333);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        if (requestCode == 333) {
            List<String> pathList = data.getStringArrayListExtra("result");
            String tvResult = "";
            FlexboxLayout groupParent = mDataBind.pictureGroup;
            for (String path : pathList) {
                PostAddImageItemBinding binding = DataBindingUtil.inflate(mLayoutInflater
                    , R.layout.post_add_image_item, groupParent, false);
                groupParent.addView(binding.getRoot(), 0);
                uploadImage(binding, path);
                tvResult += path + "\n";
            }
            if (groupParent.getChildCount() > 5) {
                groupParent.removeViews(5, groupParent.getChildCount());
            }
            showText(tvResult);
        }
    }

    /**
     * 遍历上传图片
     */
    public void uploadImage(PostAddImageItemBinding binding, String localUri) {
        UploadImageBean bean = new UploadImageBean();
        bean.setMLocalUri(localUri);
        binding.getRoot().setTag(bean);

        File file = new File(localUri);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        // api 上传
        addSubscribe(RetrofitHelper.getApis().uploadImage(body)
            .compose(RxHelper.toMain())
            .subscribe(respBean -> {
                binding.progress.setVisibility(View.GONE);

                bean.setMAbsoluteUrl(respBean.getData().getMAbsoluteUrl())
                    .setMRelativeUrl(respBean.getData().getMRelativeUrl());
                ImageLoader.loadImage(binding.image, respBean.getData().getMAbsoluteUrl());
                if (respBean.isCodeFail()) {
                    showText(respBean.getMsg());
                }
            }, RxHelper.getErrorTextConsumer(this)));

    }
}
