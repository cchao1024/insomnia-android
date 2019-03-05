package com.cchao.insomnia.ui.global;

import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.insomnia.R;
import com.cchao.insomnia.databinding.ImageShowActivityBinding;
import com.cchao.insomnia.global.Constants;
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
