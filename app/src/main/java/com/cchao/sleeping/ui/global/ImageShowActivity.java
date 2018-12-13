package com.cchao.sleeping.ui.global;

import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.sleeping.R;
import com.cchao.sleeping.databinding.ImageShowActivityBinding;
import com.cchao.sleeping.global.Constants;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * @author cchao
 * @version 8/12/18.
 */
public class ImageShowActivity extends BaseToolbarActivity<ImageShowActivityBinding> {

    PhotoView mPhotoView;
    String mImageUrl;

    @Override
    protected int getLayout() {
        return R.layout.image_show_activity;
    }

    @Override
    protected void initEventAndData() {
        mImageUrl = getIntent().getStringExtra(Constants.Extra.IMAGE_URL);
        mPhotoView = mDataBind.photoView;
        onLoadData();
    }

    @Override
    protected void onLoadData() {
        ImageLoader.loadImage(mContext, mImageUrl, mPhotoView);
    }
}
