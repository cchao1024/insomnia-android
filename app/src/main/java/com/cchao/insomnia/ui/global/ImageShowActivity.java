package com.cchao.insomnia.ui.global;

import com.cchao.insomnia.R;
import com.cchao.insomnia.databinding.ImageShowActivityBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.simplelib.core.GlideApp;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * @author cchao
 * @version 8/12/18.
 */
public class ImageShowActivity extends BaseTitleBarActivity<ImageShowActivityBinding> {

    PhotoView mPhotoView;
    String mImageUrl;

    @Override
    protected int getLayout() {
        return R.layout.image_show_activity;
    }

    @Override
    protected void initEventAndData() {
        setTitleText("浏览大图");
        mImageUrl = getIntent().getStringExtra(Constants.Extra.IMAGE_URL);
        mPhotoView = mDataBind.photoView;
        onLoadData();
    }

    @Override
    protected void onLoadData() {
        GlideApp.with(mContext)
            .load(mImageUrl)
            .centerInside()
            .into(mPhotoView);
    }
}
