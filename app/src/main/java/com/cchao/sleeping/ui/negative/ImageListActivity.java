package com.cchao.sleeping.ui.negative;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.util.DeviceInfo;
import com.cchao.simplelib.util.UIUtils;
import com.cchao.simplelib.view.itemdecoration.GridOffsetsItemDecoration;
import com.cchao.sleeping.R;
import com.cchao.sleeping.api.RetrofitHelper;
import com.cchao.sleeping.global.Constants;
import com.cchao.sleeping.model.javabean.RespListBean;
import com.cchao.sleeping.model.javabean.fall.FallImage;
import com.cchao.sleeping.ui.global.BaseListActivity;
import com.cchao.sleeping.ui.global.ImageShowActivity;
import com.cchao.sleeping.util.ImageHelper;
import com.cchao.sleeping.view.adapter.DataBindQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import io.reactivex.Observable;

/**
 * @author cchao
 * @version 8/12/18.
 */
public class ImageListActivity extends BaseListActivity<FallImage> {

    int itemWidth = DeviceInfo.getScreenWidth() / 2 - UIUtils.dp2px(15);

    @Override
    protected void initAdapter() {
        super.initAdapter();
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2
            , StaggeredGridLayoutManager.VERTICAL));
        GridOffsetsItemDecoration itemDecoration;
        mRecyclerView.addItemDecoration(itemDecoration = new GridOffsetsItemDecoration(GridOffsetsItemDecoration.GRID_OFFSETS_VERTICAL));
        itemDecoration.setHorizontalItemOffsets(UiHelper.dp2dx(10));
        itemDecoration.setVerticalItemOffsets(UiHelper.dp2dx(12));
    }

    @Override
    protected void onConvertItem(DataBindQuickAdapter.DataBindViewHolder helper, FallImage item) {
        ImageView imageView = helper.getView(R.id.image);
//                helper.getBinding().setVariable(BR.item,item);
        helper.itemView.getLayoutParams().height = ImageHelper.getScaleHeight(itemWidth, item.getWidth(), item.getHeight());

        ImageLoader.loadImageCrop(mContext, item.getUrl(), imageView);
    }

    @Override
    protected void onItemClicked(BaseQuickAdapter adapter, View view, int position) {
        Router.turnTo(mContext, ImageShowActivity.class)
            .putExtra(Constants.Extra.IMAGE_URL, mAdapter.getData().get(position).getUrl())
            .start();
    }

    @Override
    protected Observable<RespListBean<FallImage>> getLoadObservable(int page) {
        return RetrofitHelper.getApis().getImageList(page);
    }
}
