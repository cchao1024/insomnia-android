package com.cchao.insomnia.ui.global;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;

import com.cchao.insomnia.R;
import com.cchao.insomnia.databinding.ImageShowActivityBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.util.ImageHelper;
import com.cchao.simplelib.core.GlideApp;
import com.cchao.simplelib.core.Logs;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

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
        addTitleMenuItem(R.drawable.download_cloud, view -> {
            showText(R.string.developing);
//            saveImage(mPhotoView);
        });
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

    /**
     * 保存图片
     */
    private void saveImage(ImageView imageView) {
        if (imageView == null || imageView.getDrawable() == null) {
            return;
        }
        try {
            String rootPath = getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator;
            String savePath = rootPath + System.currentTimeMillis() + ".gif";

            // 开线程保存图片
            addSubscribe(Observable.create((ObservableOnSubscribe<Boolean>) e -> {
                boolean result = false;
                try {
                    result = ImageHelper.save(((BitmapDrawable) imageView.getDrawable()).getBitmap()
                        , new File(savePath), Bitmap.CompressFormat.JPEG, false);
                } catch (Exception ex) {
                    e.onError(ex);
                }
                e.onNext(result);
                e.onComplete();
            }).compose(RxHelper.toMain())
                .subscribe(s -> {
                    if (s) {
                        showText("save to " + rootPath);
                    }
                }, RxHelper.getErrorTextConsumer(this)));
        } catch (Exception exception) {
            Logs.logException(exception);
        }
    }
}
